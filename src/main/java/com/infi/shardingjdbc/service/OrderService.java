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
package com.infi.shardingjdbc.service;

import java.util.List;

import com.infi.shardingjdbc.domain.vo.OrderVo;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月3日 上午11:36:49
 * @since JDK 1.8
 */
public interface OrderService {

  public void createOrder(OrderVo orderVo);

  public void deleteByOrderId(String orderId);

  public OrderVo getOrderByOrderId(String orderId);
  
  public List<OrderVo> getOrdersByUserId(Long userId);
}
