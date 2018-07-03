package com.ongoni.onlineshop.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun greeting(): String {
        return "{\"hello\": \"world!\"}"
    }

}