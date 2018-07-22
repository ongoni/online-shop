package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Cart
import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.service.CartService
import com.ongoni.onlineshop.service.SessionService
import com.ongoni.onlineshop.service.UserService
import com.ongoni.onlineshop.utils.hashed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RegistrationController {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var sessionService: SessionService
    @Autowired
    private lateinit var cartService: CartService

    @PostMapping("/register", consumes = ["application/json"], produces = ["application/json"])
    fun register(@RequestBody user: User): ResponseEntity<Map<String, Any>> {
        if (userService.findOneByUsername(user.username).isPresent
                || (!user.email.isEmpty() && userService.findOneByEmail(user.email).isPresent)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Username or email is already used"),
                    HttpStatus.BAD_REQUEST
            )
        }

        user.password = user.password.hashed()
        user.roles = mutableSetOf(Role.USER)

        val savedUser = userService.save(user)

        val session = sessionService.save(
                sessionService.create(user)
        )

        cartService.save(Cart(user = savedUser))

        return ResponseEntity(
                mapOf("user" to user.serialized(), "access_token" to session.token),
                HttpStatus.OK
        )
    }

}