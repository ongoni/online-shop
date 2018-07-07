package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.service.ShopService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/shops")
class ShopController {
    @Autowired
    private lateinit var shopService: ShopService

    @PostMapping
    fun create() {

    }

    @GetMapping
    fun info() {

    }

    @PutMapping
    fun update() {

    }

    @DeleteMapping
    fun delete() {

    }

}