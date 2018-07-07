package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Order
import com.ongoni.onlineshop.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<Order, Long> {

    fun findAllByUser(user: User)

}