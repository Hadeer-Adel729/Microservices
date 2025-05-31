// UserServiceClient.java
package com.orderservice.client;

import com.orderservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;

    @Value("${service.url.user-service}")
    private String userServiceUrl;

    public UserDto getUserById(Long userId) {
        return restTemplate.getForObject(
                userServiceUrl + "/{id}",
                UserDto.class,
                userId
        );
    }
}