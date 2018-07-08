package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Product
import com.ongoni.onlineshop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService {
    @Autowired
    private lateinit var productRepository: ProductRepository

    fun findById(id: Long): Optional<Product> = productRepository.findById(id)

    fun save(product: Product) = productRepository.saveAndFlush(product)

    fun deleteById(id: Long) = productRepository.deleteById(id)

}