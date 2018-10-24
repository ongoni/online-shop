package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.service.OrderService
import com.ongoni.onlineshop.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.math.roundToLong

@RestController
class PaymentController {
    @Autowired
    private lateinit var orderService: OrderService
    @Autowired
    private lateinit var sessionService: SessionService

    @PostMapping("/payments", consumes = ["application/json"], produces = ["application/json"])
    fun accept(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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

        val id = (map["id"] as Double).roundToLong()
        val order = orderService.findById(id)
        if (!order.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Order not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (order.get().isPaid) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Order is already paid"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (order.get().user.id != session.get().user.id) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "You can't pay someone else's order"),
                    HttpStatus.BAD_REQUEST
            )
        }

        order.get().isPaid = true
        order.get().isDone = true
        orderService.save(order.get())

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/payments", consumes = ["application/json"], produces = ["application/json"])
    fun rollback(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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

        val id = (map["id"] as Double).roundToLong()
        val order = orderService.findById(id)
        if (!order.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Order not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (!order.get().isPaid) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Order is not paid"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (order.get().user.id != session.get().user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "You can't rollback someone else's payment"),
                    HttpStatus.BAD_REQUEST
            )
        }

        order.get().isPaid = false
        orderService.save(order.get())

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

}