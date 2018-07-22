package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Category
import com.ongoni.onlineshop.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CategoryService {
    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    fun findById(id: Long) = categoryRepository.findById(id)

    fun findAll(): MutableList<Category> = categoryRepository.findAll()

    fun findAll(pageable: Pageable) = categoryRepository.findAll(pageable)

    fun save(product: Category) = categoryRepository.saveAndFlush(product)

    fun deleteById(id: Long) = categoryRepository.deleteById(id)

}