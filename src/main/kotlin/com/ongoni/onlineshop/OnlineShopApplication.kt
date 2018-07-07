package com.ongoni.onlineshop

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration(exclude = [JacksonAutoConfiguration::class])
class OnlineShopApplication {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            runApplication<OnlineShopApplication>(*args)
        }
    }

}
