package com.jabar.orderservice;

import lombok.Builder;

@Builder
public record Product(
        Long id,
        String name,
        Double price) {
}
