package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.service.SessionService
import com.ongoni.onlineshop.service.UserService
import com.ongoni.onlineshop.utils.HashExtensions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/register")
class RegistrationController : HashExtensions {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var sessionService: SessionService

    @PostMapping(produces = ["application/json"])
    fun register(@RequestParam username: String, @RequestParam password: String): Map<String, Any> {
        if (userService.findOneByUsername(username).isPresent) {
            return mapOf(
                    "error" to true,
                    "message" to "Username is already used"
            )
        }

        val user = User(username = username, password = password.hashed())
        val session = sessionService.create(user)

        userService.save(user)
        sessionService.save(session)

        return mapOf(
                "id" to user.id,
                "access_token" to session.token
        )
    }

}