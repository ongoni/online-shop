package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.entity.Shop
import com.ongoni.onlineshop.service.SessionService
import com.ongoni.onlineshop.service.ShopService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@RestController
class ShopController {
    @Autowired
    private lateinit var shopService: ShopService
    @Autowired
    private lateinit var sessionService: SessionService

    @PostMapping("/shops", consumes = ["application/json"], produces = ["application/json"])
    fun create(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        val token = map["access_token"]?.toString() ?: return ResponseEntity(
                mapOf("error" to true, "message" to "Invalid token"),
                HttpStatus.UNAUTHORIZED
        )

        val session = sessionService.findByToken(token)
        if (token.isBlank() || (!token.isBlank()) && !session.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val user = session.get().user
        if (shopService.findByUser(user).isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Only one shop can be created"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (!map.keys.containsAll(listOf("name", "description"))) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Required arguments are missing"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val name = if (map["name"] is String) map["name"].toString() else ""
        val description = if (map["description"] is String) map["description"].toString() else ""

        if (name == "" || description == "") {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "One or few arguments are empty"),
                    HttpStatus.BAD_REQUEST
            )
        }

        user.isShopOwner = true
        val shop = shopService.save(
                Shop(
                        name = name,
                        description = description,
                        user = user
                )
        )

        return ResponseEntity(
                mapOf("error" to false, "shop" to shop.serialized()),
                HttpStatus.OK
        )
    }

    @GetMapping("/shops/{id}", consumes = ["application/json"], produces = ["application/json"])
    fun info(@PathVariable("id") id: Long): ResponseEntity<Map<String, Any>>  {
        val shop = shopService.findById(id)
        if (!shop.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Shop not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
                mapOf("error" to false, "shop" to shop.get().serialized(true)),
                HttpStatus.OK
        )
    }

    @PutMapping("/shops", consumes = ["application/json"], produces = ["application/json"])
    fun update(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>>  {
        val id = (map["id"]!! as Double).roundToLong()
        val token = map["access_token"]?.toString() ?: return ResponseEntity(
                mapOf("error" to true, "message" to "Invalid token"),
                HttpStatus.UNAUTHORIZED
        )

        val session = sessionService.findByToken(token)
        if (!session.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val shop = shopService.findById(id)
        if (!shop.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Shop not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (session.get().user.id != shop.get().user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        if (!map.keys.any { x -> x == "name" || x == "description" }) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "At least one argument required"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (map["name"] is String) {
            shop.get().name = map["name"].toString()
        }
        if (map["description"] is String) {
            shop.get().description = map["description"].toString()
        }

        return ResponseEntity(
                mapOf("error" to false, "shop" to shopService.save(shop.get()).serialized(true)),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/shops", consumes = ["application/json"], produces = ["application/json"])
    fun delete(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>>  {
        val id = (map["id"]!! as Double).roundToLong()
        val token = map["access_token"]?.toString() ?: return ResponseEntity(
                mapOf("error" to true, "message" to "Invalid token"),
                HttpStatus.UNAUTHORIZED
        )

        val session = sessionService.findByToken(token)
        if (!session.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val shop = shopService.findById(id)
        if (!shop.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Shop not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (session.get().user.id != shop.get().user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        session.get().user.isShopOwner = false
        shopService.deleteById(shop.get().id)

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

    @PostMapping("/shops/all", consumes = ["application/json"], produces = ["application/json"])
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

        val page = shopService.findAll(PageRequest(pageNumber, limit))

        return ResponseEntity(
                mapOf("error" to false, "shops" to page.content.serialized(), "total" to page.totalElements),
                HttpStatus.OK
        )
    }

    fun Collection<Shop>.serialized() = this.map { it.serialized() }.toList()

}