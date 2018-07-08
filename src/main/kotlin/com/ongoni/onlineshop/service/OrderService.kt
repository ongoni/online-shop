package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Order
import com.ongoni.onlineshop.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService {
    @Autowired
    private lateinit var orderRepository: OrderRepository

    fun findById(id: Long): Optional<Order> = orderRepository.findById(id)

    fun save(order: Order) = orderRepository.saveAndFlush(order)

    fun deleteById(id: Long) = orderRepository.deleteById(id)

}