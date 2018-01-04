package com.infi.shardingjdbc.internal.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;

    private String orderId;

    private String description;

    private Long userId;

    private Long retailerId;

    private Integer isValid;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}