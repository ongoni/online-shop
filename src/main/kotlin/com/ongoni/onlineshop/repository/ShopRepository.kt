package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository

interface ShopRepository : JpaRepository<Shop, Long> {
}