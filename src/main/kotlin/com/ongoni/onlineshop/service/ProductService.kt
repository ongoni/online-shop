package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Category
import com.ongoni.onlineshop.entity.Product
import com.ongoni.onlineshop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService {
    @Autowired
    private lateinit var productRepository: ProductRepository

    fun findById(id: Long) = productRepository.findById(id)

    fun findByName(name: String) = productRepository.findOneByName(name)

    fun findAll(pageable: Pageable) = productRepository.findAll(pageable)

    fun findAllByNameLike(name: String) = productRepository.findAllByNameStartingWith(name)

    fun findAllByNameLike(name: String, pageable: Pageable) = productRepository.findAllByNameStartingWith(name, pageable)

    fun findAllByCategory(category: Category) = productRepository.findAllByCategory(category)

    fun save(product: Product) = productRepository.saveAndFlush(product)

    fun deleteById(id: Long) = productRepository.deleteById(id)

}