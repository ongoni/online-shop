package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService {
    @Autowired
    private lateinit var orderRepository: OrderRepository



}