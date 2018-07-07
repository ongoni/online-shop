package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping
    fun auth() {

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