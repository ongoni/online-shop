package com.ongoni.onlineshop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OnlineShopApplication {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            runApplication<OnlineShopApplication>(*args)
        }
    }

}
