package com.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto implements Serializable{
    private Long id;
    private UserDto user;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
}