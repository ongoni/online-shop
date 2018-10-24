package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Cart
import com.ongoni.onlineshop.service.CartService
import com.ongoni.onlineshop.service.ProductService
import com.ongoni.onlineshop.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.math.roundToLong

@RestController
class CartController {
    @Autowired
    private lateinit var sessionService: SessionService
    @Autowired
    private lateinit var cartService: CartService
    @Autowired
    private lateinit var productService: ProductService

    @PostMapping("/carts", consumes = ["application/json"], produces = ["application/json"])
    fun addProduct(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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
        val product = productService.findById(id)
        if (!product.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (product.get().amount == 0) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product is out of stock"),
                    HttpStatus.BAD_REQUEST
            )
        }

        var cart = cartService.findByUser(session.get().user)
        if (!cart.isPresent) {
            cart = Optional.of(cartService.save(Cart(user = session.get().user)))
        }

        cart.get().products.add(product.get())
        product.get().amount--
        productService.save(product.get())

        return ResponseEntity(
                mapOf("error" to false, "cart" to cart.get().serialized()),
                HttpStatus.OK
        )
    }

    @GetMapping("/carts/{access_token}", consumes = ["application/json"], produces = ["application/json"])
    fun info(@PathVariable("access_token") accessToken: String): ResponseEntity<Map<String, Any>> {
        val session = sessionService.findByToken(accessToken)
        if (accessToken.isBlank() || (!accessToken.isBlank()) && !session.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Invalid token"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        val cart = cartService.findByUser(session.get().user)

        return ResponseEntity(
                mapOf("error" to false, "cart" to cart.get().serialized()),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/carts", consumes = ["application/json"], produces = ["application/json"])
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

        val cart = cartService.findByUser(session.get().user)
        val id = (map["id"] as Double).roundToLong()
        if (!cart.get().products.any{ x -> x.id == id }) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        val product = cart.get().products.first { x -> x.id == id }
        cart.get().products.remove(product)
        product.amount++

        cartService.save(cart.get())
        productService.save(product)

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

}