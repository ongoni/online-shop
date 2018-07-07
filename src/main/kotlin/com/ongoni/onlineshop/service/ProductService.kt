package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {
    @Autowired
    private lateinit var productRepository: ProductRepository



}