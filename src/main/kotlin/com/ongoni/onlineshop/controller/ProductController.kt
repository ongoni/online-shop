package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController {
    @Autowired
    private lateinit var productService: ProductService

    @PostMapping
    fun add() {

    }

    @GetMapping
    fun info() {

    }

    @PutMapping
    fun update() {

    }

    @DeleteMapping
    fun delete() {

    }

}