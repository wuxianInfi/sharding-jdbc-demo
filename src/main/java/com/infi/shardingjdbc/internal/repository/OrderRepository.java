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
package com.infi.shardingjdbc.internal.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.infi.shardingjdbc.internal.entity.Order;
import com.infi.shardingjdbc.internal.mapper.OrderMapper;

import io.shardingjdbc.core.api.HintManager;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月4日 下午12:41:18
 * @since JDK 1.8
 */
@Repository
public class OrderRepository {

  @Autowired
  private OrderMapper orderMapper;

  public void deleteByOrderId(String orderId) {
    orderMapper.deleteByOrderId(orderId);
  }

  public boolean insertSelective(Order record) {
    return orderMapper.insertSelective(record) == 1;
  }

  public Order selectByOrderId(String orderId) {
    return orderMapper.selectByOrderId(orderId);
  }

  public Order selectByOrderIdBindMaster(String orderId) {
    try (HintManager hintManager = HintManager.getInstance()) {
      hintManager.setMasterRouteOnly();
      return orderMapper.selectByOrderId(orderId);
    }
  }

  public List<Order> selectByUserIdBindMaster(Long userId) {
    try (HintManager hintManager = HintManager.getInstance()) {
      hintManager.setMasterRouteOnly();
      return orderMapper.selectByUserId(userId);
    }
  }

}
