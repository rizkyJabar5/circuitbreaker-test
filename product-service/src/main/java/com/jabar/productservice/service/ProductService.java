package com.jabar.productservice.service;

import com.jabar.productservice.Product;
import com.jabar.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> getAllProduct() {

        return productRepository.findAll();
    }
}

