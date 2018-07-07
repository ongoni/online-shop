package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.repository.ShopRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShopService {
    @Autowired
    private lateinit var shopRepository: ShopRepository



}