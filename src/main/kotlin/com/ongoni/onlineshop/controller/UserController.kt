package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.service.SessionService
import com.ongoni.onlineshop.service.UserService
import com.ongoni.onlineshop.utils.hashed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@RestController
class UserController {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var sessionService: SessionService

    @PostMapping("/users", consumes = ["application/json"], produces = ["application/json"])
    fun auth(@RequestBody user: User): ResponseEntity<Map<String, Any>> {
        val foundUser = userService.findOneByUsername(user.username)
        if (!foundUser.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "User not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (foundUser.get().password != user.password.hashed()) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Incorrect password"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val session = sessionService.create(foundUser.get())
        sessionService.save(session)

        return ResponseEntity(
                mapOf("error" to false, "access_token" to session.token),
                HttpStatus.OK
        )
    }

    @GetMapping("/users/{id}", consumes = ["application/json"], produces = ["application/json"])
    fun info(@PathVariable("id") id: Long): ResponseEntity<Map<String, Any>> {
        val user = userService.findById(id)
        if (!user.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "User not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
                mapOf("error" to false, "user" to user.get().serialized()),
                HttpStatus.OK
        )
    }

    @PutMapping("/users", consumes = ["application/json"], produces = ["application/json"])
    fun update(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        val id = (map["id"]!! as Double).roundToLong()
        val token = map["access_token"]?.toString() ?: return ResponseEntity(
                mapOf("error" to true, "message" to "Invalid token"),
                HttpStatus.UNAUTHORIZED
        )

        if (token.isBlank() || (!token.isBlank()) && !sessionService.findByToken(token).isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val user = userService.findById(id)
        if (!user.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "User not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        val session = sessionService.findByToken(token)
        if (session.get().user.id != id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        if (!map.keys.any { x -> x =="password" || x == "first_name" || x == "last_name" || x == "email"}) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "At least one argument required"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (map["password"] is String) {
            user.get().password = map["password"].toString().hashed()
        }
        if (map["first_name"] is String) {
            user.get().firstName = map["first_name"].toString()
        }
        if (map["last_name"] is String) {
            user.get().lastName = map["last_name"].toString()
        }
        if (map["email"] is String) {
            val email = map["email"].toString()

            if (!userService.findOneByEmail(email).isPresent) {
                user.get().email = email
            } else {
                return ResponseEntity(
                        mapOf("error" to true, "message" to "Email is already used"),
                        HttpStatus.BAD_REQUEST
                )
            }
        }

        userService.save(user.get())

        return ResponseEntity(
                mapOf("error" to false, "user" to user.get().serialized()),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/users", consumes = ["application/json"], produces = ["application/json"])
    fun delete(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        val id = (map["id"]!! as Double).roundToLong()
        val token = map["access_token"] as String

        val session = sessionService.findByToken(token)
        if (!session.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val user = userService.findById(id)
        if (!user.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "User not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if ((session.get().user.roles.contains(Role.SUPER) && user.get().roles.contains(Role.SUPER))
                || !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        userService.deleteById(user.get().id)

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

    @PostMapping("/users/all", consumes = ["application/json"], produces = ["application/json"])
    fun all(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        val pageNumber = if (map["page"] is Double) {
            (map["page"] as Double).roundToInt() - 1
        } else {
            0
        }
        val limit = if (map["limit"] is Double) {
            (map["limit"] as Double).roundToInt()
        } else {
            0
        }

        val page = userService.findAll(PageRequest(pageNumber, limit))

        return ResponseEntity(
                mapOf("error" to false, "users" to page.content.serialized(), "total" to page.totalElements),
                HttpStatus.OK
        )
    }

    fun Collection<User>.serialized() = this.map { it.serialized() }.toList()
}