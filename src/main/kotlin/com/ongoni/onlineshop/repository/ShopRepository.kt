package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Shop
import com.ongoni.onlineshop.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ShopRepository : JpaRepository<Shop, Long> {

    fun findOneByUser(user: User): Optional<Shop>

}