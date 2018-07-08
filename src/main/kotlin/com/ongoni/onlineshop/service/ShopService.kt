package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Shop
import com.ongoni.onlineshop.repository.ShopRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShopService {
    @Autowired
    private lateinit var shopRepository: ShopRepository

    fun findById(id: Long): Optional<Shop> = shopRepository.findById(id)

    fun save(shop: Shop) = shopRepository.saveAndFlush(shop)

    fun deleteById(id: Long) = shopRepository.deleteById(id)

}