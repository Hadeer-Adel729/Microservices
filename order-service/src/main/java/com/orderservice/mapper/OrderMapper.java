// OrderMapper.java (updated)
package com.orderservice.mapper;

import com.orderservice.dto.OrderDetailsDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.ProductDto;
import com.orderservice.dto.UserDto;
import com.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderDetailsDto toDetailsDto(Order order);

    @Named("enrichWithUserAndProduct")
    default OrderDetailsDto enrichWithExternalData(Order order, UserDto user, ProductDto product) {
        OrderDetailsDto dto = toDetailsDto(order);
        dto.setUser(user);
        dto.setProduct(product);
        return dto;
    }
}
