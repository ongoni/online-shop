package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Cart
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.repository.CartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartService {
    @Autowired
    private lateinit var cartRepository: CartRepository

    fun findById(id: Long) = cartRepository.findById(id)

    fun findByUser(user: User) = cartRepository.findByUser(user)

    fun findAll(): MutableList<Cart> = cartRepository.findAll()

    fun save(cart: Cart) = cartRepository.saveAndFlush(cart)

    fun deleteById(id: Long) = cartRepository.deleteById(id)


}