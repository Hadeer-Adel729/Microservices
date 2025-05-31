// ProductServiceClient.java
package com.orderservice.client;

import com.orderservice.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestTemplate restTemplate;

    @Value("${service.url.product-service}")
    private String productServiceUrl;

    public ProductDto getProductById(Long productId) {
        return restTemplate.getForObject(
                productServiceUrl + "/{id}",
                ProductDto.class,
                productId
        );
    }
}