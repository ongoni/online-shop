package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController {
    @Autowired
    private lateinit var orderService: OrderService

    @PostMapping
    fun create() {

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