package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Shop
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.repository.ShopRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ShopService {
    @Autowired
    private lateinit var shopRepository: ShopRepository

    fun findById(id: Long) = shopRepository.findById(id)

    fun findByUser(user: User) = shopRepository.findOneByUser(user)

    fun findAll(): MutableList<Shop> = shopRepository.findAll()

    fun findAll(pageable: Pageable) = shopRepository.findAll(pageable)

    fun save(shop: Shop) = shopRepository.saveAndFlush(shop)

    fun deleteById(id: Long) = shopRepository.deleteById(id)

}