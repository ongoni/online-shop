package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
}