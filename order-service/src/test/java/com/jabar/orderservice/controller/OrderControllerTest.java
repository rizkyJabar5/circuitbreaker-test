package com.jabar.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabar.orderservice.Product;
import com.jabar.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@CircuitBreaker(name = "orderService", fallbackMethod = "onError")
class OrderControllerTest {
    @Autowired
    OrderController underTest;

    @Autowired
    MockMvc mvc;
    @MockBean
    OrderService orderService;

    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    void setUp() {
        circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
    }

    @Test
    void getAllProducts_WhenIsAvailableProductService() throws Exception {
        when(orderService.getAllProduct()).thenReturn(findAll());

        mvc.perform(
                get("/api/v1/orders").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            String contentAsString = result.getResponse().getContentAsString();
//            Iterable<Product> response = stringToObject(
//                    contentAsString,
//                    new TypeReference<>() {}
//            );

            assertThat(contentAsString)
//                    .isEqualTo(findAll())
                    .asList()
                    .isEmpty()
            ;
        });
    }

    <T> String objectAsString(T obj) {
        var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    <T> T stringToObject(String obj, TypeReference<T> reference) {
        var mapper = new ObjectMapper();
        try {
            return mapper.readValue(obj, reference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Product> findAll() {
        Product clothes = Product.builder()
                .id(1L)
                .name("Clothes")
                .price(10000.0)
                .build();
        Product jeans = Product.builder()
                .id(2L)
                .name("Jeans")
                .price(130000.0)
                .build();
        Product watches = Product.builder()
                .id(3L)
                .name("Expeditions")
                .price(10000.0)
                .build();

        return List.of(clothes, jeans, watches);
    }

    private CompletionStage<OrderService.BaseResponse<List<Product>>> onError(Exception e) {
        return CompletableFuture.completedFuture(OrderService.BaseResponse.<List<Product>>builder()
                .errorCode(String.valueOf(HttpStatus.OK))
                .message(String.format("Error: %s", e.getMessage()))
                .data(Collections.emptyList())
                .build());
    }
}