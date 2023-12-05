package com.jabar.orderservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jabar.orderservice.ControllerAdvisor.ThirdServiceException;
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
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class OrderService {
    public static final String PRODUCT_SERVICE_URL = "http://localhost:9090/api/v1/products";
    public static final String SERVICE_NAME = "order-service";

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "onError")
    public BaseResponse<Iterable<Product>> getAllProduct() {
        ResponseEntity<Iterable<Product>> response = restTemplate.exchange(
                PRODUCT_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        var body = response.getBody();
        var counter = new AtomicInteger(0);

        if (response.getStatusCode().is2xxSuccessful())
            body.iterator().forEachRemaining(c -> counter.incrementAndGet());


        return BaseResponse.<Iterable<Product>>builder()
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(String.format("Record found %s for products", counter))
                .data(body)
                .build();
    }

    private BaseResponse<Void> onError(Throwable e) {
        throw new ThirdServiceException(e.getMessage());
    }

    @Builder
    @JsonInclude(Include.NON_NULL)
    public record BaseResponse<T>(
            String errorCode,
            String message,
            T data) implements Serializable {
    }
}
