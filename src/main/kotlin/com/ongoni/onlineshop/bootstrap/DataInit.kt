package com.ongoni.onlineshop.bootstrap

import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class DataInit : ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private lateinit var userService: UserService

    override fun onApplicationEvent(p0: ContextRefreshedEvent) {
        if (!userService.findOneByUsername("ongoni").isPresent) {
            userService.save(User(
                    username = "ongoni",
                    password = "b5ba77af1f7bda735894e746a199acb1d2c836424da2fc46bebb55423dccbff871877a30fab77a31e47b0a29ea0154882e532e9a29b220a8f2958773313bbb2a",
                    email = "ongoni.fg@gmail.com",
                    firstName = "Alexander",
                    lastName = "Krivonozhkin",
                    roles = mutableSetOf(Role.USER, Role.SUPER)
            ))
        }
    }
}