package com.orderservice.service;

import com.orderservice.client.ProductServiceClient;
import com.orderservice.client.UserServiceClient;
import com.orderservice.dto.OrderDetailsDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.ProductDto;
import com.orderservice.dto.UserDto;
import com.orderservice.entity.Order;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;

    public OrderDetailsDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        UserDto user = userServiceClient.getUserById(order.getUserId());
        ProductDto product = productServiceClient.getProductById(order.getProductId());

        return orderMapper.enrichWithExternalData(order, user, product);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }



        public OrderDto createOrder(OrderDto orderDto) {
        // Validate user exists
        UserDto user = userServiceClient.getUserById(orderDto.getUserId());
        if (user == null) throw new RuntimeException("User not found");

        // Validate product exists
        ProductDto product = productServiceClient.getProductById(orderDto.getProductId());
        if (product == null) throw new RuntimeException("Product not found");

        // Calculate total price
        orderDto.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(orderDto.getQuantity())));
        orderDto.setOrderDate(LocalDateTime.now());

        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
}