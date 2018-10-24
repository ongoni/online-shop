package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {

}