package com.jabar.orderservice.controller;

import com.jabar.orderservice.Product;
import com.jabar.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public CompletionStage<OrderService.BaseResponse<Iterable<Product>>> getAllProducts() {
        return CompletableFuture.supplyAsync(this.orderService::getAllProduct);
    }
}