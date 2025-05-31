package com.orderservice.service;

import com.orderservice.dto.OrderDetailsDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.ProductDto;
import com.orderservice.dto.UserDto;
import com.orderservice.entity.Order;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8081/api/users";
    private static final String PRODUCT_SERVICE_URL = "http://localhost:8083/api/products";

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderDetailsDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Fetch user details from User Service
        UserDto user = restTemplate.getForObject(
                USER_SERVICE_URL + "/" + order.getUserId(),
                UserDto.class
        );

        // Fetch product details from Product Service
        ProductDto product = restTemplate.getForObject(
                PRODUCT_SERVICE_URL + "/" + order.getProductId(),
                ProductDto.class
        );

        // Create order details with full information
        OrderDetailsDto orderDetails = new OrderDetailsDto();
        orderDetails.setId(order.getId());
        orderDetails.setUser(user);
        orderDetails.setProduct(product);
        orderDetails.setQuantity(order.getQuantity());
        orderDetails.setTotalPrice(order.getTotalPrice());
        orderDetails.setOrderDate(order.getOrderDate());

        return orderDetails;
    }

    public OrderDto createOrder(OrderDto orderDto) {
        // Validate user exists
        UserDto user = restTemplate.getForObject(
                USER_SERVICE_URL + "/" + orderDto.getUserId(),
                UserDto.class
        );
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate product exists and get price
        ProductDto product = restTemplate.getForObject(
                PRODUCT_SERVICE_URL + "/" + orderDto.getProductId(),
                ProductDto.class
        );
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Calculate total price
        orderDto.setTotalPrice(product.getPrice().multiply(java.math.BigDecimal.valueOf(orderDto.getQuantity())));
        orderDto.setOrderDate(LocalDateTime.now());

        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
}