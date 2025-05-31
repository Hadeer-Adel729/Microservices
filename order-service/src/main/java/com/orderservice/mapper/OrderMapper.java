package com.orderservice.mapper;

import com.orderservice.dto.OrderDto;
import com.orderservice.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);
}