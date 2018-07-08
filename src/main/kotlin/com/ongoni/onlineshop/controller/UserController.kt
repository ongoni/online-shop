package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.service.SessionService
import com.ongoni.onlineshop.service.UserService
import com.ongoni.onlineshop.utils.HashExtensions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController : HashExtensions {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var sessionService: SessionService

    @PostMapping(produces = ["application/json"])
    fun auth(@RequestParam username: String, @RequestParam password: String): Map<String, Any> {
        val user = userService.findOneByUsername(username)
        if (!user.isPresent) {
            return mapOf(
                    "error" to true,
                    "message" to "User not found"
            )
        }

        if (user.get().password != password.hashed()) {
            return mapOf(
                    "error" to true,
                    "message" to "Incorrect password"
            )
        }

        val session = sessionService.create(user.get())
        sessionService.save(session)

        return mapOf(
                "error" to false,
                "access_token" to session.token
        )
    }

    @GetMapping(produces = ["application/json"])
    fun info(@PathVariable id: Long): Map<String, Any> {
        return userService.findById(id).get().safeSerialized()
    }

    @PutMapping(produces = ["application/json"])
    fun update(): Map<String, Any> {
        return mapOf()
    }

    @DeleteMapping(produces = ["application/json"])
    fun delete(@PathVariable id: Long): Map<String, Any> {
        return mapOf()
    }

}