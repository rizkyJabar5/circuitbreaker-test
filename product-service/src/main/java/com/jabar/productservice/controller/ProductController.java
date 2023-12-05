package com.jabar.productservice.controller;

import com.jabar.productservice.Product;
import com.jabar.productservice.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterator<Product> getAllProducts() {
        return productService.getAllProduct()
                .iterator();
    }
}