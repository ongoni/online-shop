package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Category
import com.ongoni.onlineshop.entity.Product
import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.service.CategoryService
import com.ongoni.onlineshop.service.ProductService
import com.ongoni.onlineshop.service.SessionService
import com.ongoni.onlineshop.service.ShopService
import com.ongoni.onlineshop.utils.serialized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@RestController
class ProductController {
    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var sessionService: SessionService
    @Autowired
    private lateinit var shopService: ShopService
    @Autowired
    private lateinit var categoryService: CategoryService

    @PostMapping("/products", consumes = ["application/json"], produces = ["application/json"])
    fun add(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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

        val shop = shopService.findByUser(session.get().user)
        if (!shop.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Shop not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (!map.keys.containsAll(listOf("name", "description", "amount", "price"))) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Required arguments are missing"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val name = if (map["name"] is String) {
            map["name"].toString()
        } else {
            ""
        }
        val description = if (map["description"] is String) {
            map["description"].toString()
        } else {
            ""
        }
        val amount = if (map["amount"] is Double) {
            (map["amount"] as Double).roundToInt()
        } else {
            0
        }
        val price = if (map["price"] is Double) {
            map["price"] as Double
        } else {
            0.0
        }
        val categoryId = if (map["category"] is Double) {
            (map["category"] as Double).roundToInt()
        } else {
            0
        }

        if (name == "" || description == "" || amount == 0 || price == 0.0) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "One or few arguments are empty"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (shopService.findByUser(session.get().user).get().products.any { x -> x.name == name }) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product with this name already exists"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (amount < 0 || price < 0) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Amount or price can't be negative"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val category: Optional<Category>?
        if (categoryId != 0) {
            category = categoryService.findById(categoryId.toLong())
            if (!category.isPresent) {
                return ResponseEntity(
                        mapOf("error" to true, "message" to "Category not found"),
                        HttpStatus.OK
                )
            }
        } else {
            category = null
        }

        val product = productService.save(
                Product(
                        name = name,
                        description = description,
                        amount = amount,
                        price = price,
                        category = category?.get(),
                        shop = shop.get()
                )
        )

        shop.get().products.add(product)
        shopService.save(shop.get())

        return ResponseEntity(
                mapOf("error" to false, "product" to product.serialized()),
                HttpStatus.OK
        )
    }

    @GetMapping("/products/{id}", consumes = ["application/json"], produces = ["application/json"])
    fun info(@PathVariable("id") id: Long): ResponseEntity<Map<String, Any>> {
        val product = productService.findById(id)
        if (!product.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
                mapOf("error" to false, "product" to product.get().serialized()),
                HttpStatus.OK
        )
    }

    @PutMapping("/products", consumes = ["application/json"], produces = ["application/json"])
    fun update(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        val id = (map["id"]!! as Double).roundToLong()
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

        val product = productService.findById(id)
        if (!product.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        val shop = shopService.findByUser(session.get().user)
        if (!shop.get().products.contains(product.get()) && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        if (!map.keys.any { x -> x == "name" || x == "description" || x == "amount" || x == "price" }) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "At least one argument required"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (map["name"] is String) {
            product.get().name = map["name"].toString()
        }
        if (map["description"] is String) {
            product.get().description = map["description"].toString()
        }
        if (map["amount"] is Double) {
            val amount = (map["amount"] as Double).roundToInt()
            if (amount < 0) {
                return ResponseEntity(
                        mapOf("error" to true, "message" to "Amount can't be negative"),
                        HttpStatus.BAD_REQUEST
                )
            }

            product.get().amount = amount
        }
        if (map["price"] is Double) {
            val price = map["price"] as Double
            if (price < 0) {
                return ResponseEntity(
                        mapOf("error" to true, "message" to "Price can't be negative"),
                        HttpStatus.BAD_REQUEST
                )
            }

            product.get().price = price
        }
        val categoryId = if (map["category"] is Double) {
            (map["category"] as Double).roundToInt()
        } else {
            0
        }

        val category: Optional<Category>?
        if (categoryId != 0) {
            category = categoryService.findById(categoryId.toLong())
            if (!category.isPresent) {
                return ResponseEntity(
                        mapOf("error" to true, "message" to "Category not found"),
                        HttpStatus.OK
                )
            }

            product.get().category = category.get()
        }

        productService.save(product.get())

        return ResponseEntity(
                mapOf("error" to false, "product" to product.get().serialized()),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/products", consumes = ["application/json"], produces = ["application/json"])
    fun delete(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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

        val product = productService.findById(id)
        if (!product.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Product not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        val shop = shopService.findByUser(session.get().user)
        if (!shop.get().products.contains(product.get()) && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        shop.get().products.remove(product.get())
        productService.deleteById(product.get().id)

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

    @PostMapping("/products/all", consumes = ["application/json"], produces = ["application/json"])
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
        val name = if (map["name"] is String) {
            map["name"].toString()
        } else {
            ""
        }

        val pageRequest = PageRequest(pageNumber, limit)
        val page = if (name != "") {
            productService.findAllByNameLike(name, pageRequest)
        } else {
            productService.findAll(pageRequest)
        }

        return ResponseEntity(
                mapOf("error" to false, "products" to page.content.serialized(), "total" to page.totalElements),
                HttpStatus.OK
        )
    }

}