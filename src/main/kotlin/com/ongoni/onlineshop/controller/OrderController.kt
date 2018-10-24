package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Order
import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.service.CartService
import com.ongoni.onlineshop.service.OrderService
import com.ongoni.onlineshop.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@RestController
class OrderController {
    @Autowired
    private lateinit var orderService: OrderService
    @Autowired
    private lateinit var sessionService: SessionService
    @Autowired
    private lateinit var cartService: CartService

    @PostMapping("/orders", consumes = ["application/json"], produces = ["application/json"])
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

        val cart = cartService.findByUser(session.get().user)
        if (cart.get().products.isEmpty()) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Cart is empty"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val order = orderService.save(
                Order(
                        user = session.get().user,
                        products = cart.get().products.toMutableList(),
                        date = Date(),
                        isPaid = false,
                        isDone = false
                )
        )

        cart.get().products.clear()
        cartService.save(cart.get())

        return ResponseEntity(
                mapOf("error" to false, "order" to order.serialized(true)),
                HttpStatus.OK
        )
    }

    @GetMapping("/orders/{access_token}/{id}", consumes = ["application/json"], produces = ["application/json"])
    fun info(@PathVariable("access_token") accessToken: String, @PathVariable("id") id: Long): ResponseEntity<Map<String, Any>> {
        val session = sessionService.findByToken(accessToken)
        if (accessToken.isBlank() || (!accessToken.isBlank()) && !session.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val order = orderService.findById(id)
        if (!order.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Order not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (order.get().user.id != session.get().user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        return ResponseEntity(
                mapOf("error" to false, "order" to order.get().serialized()),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/orders", consumes = ["application/json"], produces = ["application/json"])
    fun delete(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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

        val id = (map["id"]!! as Double).roundToLong()
        val order = orderService.findById(id)
        if (!order.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Order not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (order.get().user.id != session.get().user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        order.get().products.forEach {
            it.amount++
        }

        orderService.deleteById(id)

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

    @PostMapping("/orders/all", consumes = ["application/json"], produces = ["application/json"])
    fun all(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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

        val page = orderService.findAllByUser(session.get().user, PageRequest(pageNumber, limit))

        return ResponseEntity(
                mapOf("error" to false, "orders" to page.content.serialized(), "total" to page.totalElements),
                HttpStatus.OK
        )
    }

    fun Collection<Order>.serialized(detailed: Boolean = false) = this.map { it.serialized(detailed) }.toList()
}