package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Category
import com.ongoni.onlineshop.entity.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository : JpaRepository<Product, Long> {

    fun findOneByName(name: String): Optional<Product>

    fun findAllByNameStartingWith(name: String): MutableList<Product>

    fun findAllByNameStartingWith(name: String, pageable: Pageable): Page<Product>

    fun findAllByCategory(category: Category): MutableList<Product>

}