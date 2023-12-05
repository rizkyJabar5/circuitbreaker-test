package com.jabar.orderservice.service;

import com.jabar.orderservice.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
@Slf4j
public class OrderService {
    public static final String PRODUCT_SERVICE_URL = "http://localhost:9090/api/v1/products";

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "onError")
    public Iterable<Product> getAllProduct() {
        ResponseEntity<Iterable<Product>> response = restTemplate.exchange(
                PRODUCT_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

    private BaseResponse<List<Product>> onError(Exception e) {
        log.info("Error: {}", e.getMessage());

        return BaseResponse.<List<Product>>builder()
                .errorCode(String.valueOf(HttpStatus.OK))
                .message(String.format("Error: %s", e.getMessage()))
                .data(Collections.emptyList())
                .build();
    }

    @Builder
    public record BaseResponse<T>(
            String errorCode,
            String message,
            T data)  implements Serializable {
    }
}
