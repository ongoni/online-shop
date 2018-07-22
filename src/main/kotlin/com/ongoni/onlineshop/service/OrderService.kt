package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Order
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OrderService {
    @Autowired
    private lateinit var orderRepository: OrderRepository

    fun findById(id: Long) = orderRepository.findById(id)

    fun findAll(pageable: Pageable) = orderRepository.findAll(pageable)

    fun findAllByUser(user: User) = orderRepository.findAllByUser(user)

    fun findAllByUser(user: User, pageable: Pageable) = orderRepository.findAllByUser(user, pageable)

    fun save(order: Order) = orderRepository.saveAndFlush(order)

    fun deleteById(id: Long) = orderRepository.deleteById(id)

}