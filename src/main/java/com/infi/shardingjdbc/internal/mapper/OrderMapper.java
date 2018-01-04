package com.infi.shardingjdbc.internal.mapper;

import java.util.List;

import com.infi.shardingjdbc.internal.entity.Order;

public interface OrderMapper {
  int deleteByPrimaryKey(Long id);
  
  int deleteByOrderId(String orderId);

  int insert(Order record);

  int insertSelective(Order record);

  Order selectByPrimaryKey(Long id);
  
  Order selectByOrderId(String orderId);
  
  List<Order> selectByUserId(Long userId);

  int updateByPrimaryKeySelective(Order record);

  int updateByPrimaryKey(Order record);
}
