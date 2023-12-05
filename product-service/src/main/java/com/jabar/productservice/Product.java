package com.jabar.productservice;

import lombok.Builder;

@Builder
public record Product(
        Long id,
        String name,
        Double price) {
}
