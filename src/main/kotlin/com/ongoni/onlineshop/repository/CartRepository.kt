package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Cart
import com.ongoni.onlineshop.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CartRepository : JpaRepository<Cart, Long> {

    fun findByUser(user: User): Optional<Cart>

}