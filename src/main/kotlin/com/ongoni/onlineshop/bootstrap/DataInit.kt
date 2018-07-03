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
        userService.save(User(
                username = "ongoni",
                password = "\$2a\$10\$y2RRilwUKlvfbBfvUJ9qQ.VfB5opFpk3wh70pP3rxIJfXhqzTZBaG",
                email = "ongoni.fg@gmail.com",
                firstName = "Alexander",
                lastName = "Krivonozhkin",
                roles = mutableSetOf(Role.USER, Role.SUPER)
        ))
    }

}