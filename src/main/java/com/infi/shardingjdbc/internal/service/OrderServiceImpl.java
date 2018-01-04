/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.infi.shardingjdbc.internal.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.hash.Hashing;
import com.infi.shardingjdbc.domain.vo.OrderVo;
import com.infi.shardingjdbc.internal.entity.Order;
import com.infi.shardingjdbc.internal.repository.OrderRepository;
import com.infi.shardingjdbc.service.OrderService;

import io.shardingjdbc.core.keygen.DefaultKeyGenerator;
import io.shardingjdbc.core.keygen.KeyGenerator;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月3日 上午11:37:03
 * @since JDK 1.8
 */
@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;
  private KeyGenerator keyGenerator;

  @PostConstruct
  void init() {
    long machineId = Math.abs(
        Hashing.murmur3_128().hashString("127.0.0.1:8080", StandardCharsets.UTF_8).asLong()) % 1000;
    DefaultKeyGenerator.setWorkerId(machineId);
    this.keyGenerator = new DefaultKeyGenerator();
  }

  @Override
  @Transactional
  public void createOrder(OrderVo orderVo) {
    String orderId = keyGenerator.generateKey().toString();
    Order order = convert2Order(orderId, orderVo);
    orderRepository.insertSelective(order);
  }

  private Order convert2Order(String orderId, OrderVo orderVo) {
    return Order.builder().orderId(orderId).description(orderVo.getDescription())
        .userId(orderVo.getUserId()).retailerId(orderVo.getRetailerId()).build();
  }

  @Override
  @Transactional
  public void deleteByOrderId(String orderId) {
    orderRepository.deleteByOrderId(orderId);
  }

  @Override
  public OrderVo getOrderByOrderId(String orderId) {
    Order order = orderRepository.selectByOrderIdBindMaster(orderId);
    return order != null ? convert2OrderVo(order) : null;
  }

  @Override
  public List<OrderVo> getOrdersByUserId(Long userId) {
    return orderRepository.selectByUserIdBindMaster(userId).stream()
        .map(e -> this.convert2OrderVo(e)).collect(Collectors.toList());
  }

  private OrderVo convert2OrderVo(Order order) {
    return OrderVo.builder().orderId(order.getOrderId()).build();
  }

}
