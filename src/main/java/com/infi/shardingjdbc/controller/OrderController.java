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
package com.infi.shardingjdbc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.infi.shardingjdbc.domain.vo.OrderVo;
import com.infi.shardingjdbc.service.OrderService;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月4日 下午1:14:14
 * @since JDK 1.8
 */
@RestController
@ResponseBody
@RequestMapping("/v1/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
  public OrderVo getOrderByOrderId(@PathVariable(value = "orderId") String orderId) {
    return orderService.getOrderByOrderId(orderId);
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
  public List<OrderVo> getOrderByOrderId(@PathVariable(value = "userId") Long userId) {
    return orderService.getOrdersByUserId(userId);
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public void createOrder(OrderVo orderVo) {
    orderService.createOrder(orderVo);
  }

  @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE)
  public void deleteOrder(@PathVariable(value = "orderId") String orderId) {
    orderService.deleteByOrderId(orderId);
  }

}
