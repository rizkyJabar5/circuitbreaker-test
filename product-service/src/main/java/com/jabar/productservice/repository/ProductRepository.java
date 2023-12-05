package com.jabar.productservice.repository;

import com.jabar.productservice.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

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
}
