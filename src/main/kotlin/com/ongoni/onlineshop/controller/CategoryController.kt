package com.ongoni.onlineshop.controller

import com.ongoni.onlineshop.entity.Category
import com.ongoni.onlineshop.entity.Role
import com.ongoni.onlineshop.repository.CategoryRepository
import com.ongoni.onlineshop.service.CategoryService
import com.ongoni.onlineshop.service.ProductService
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
class CategoryController {
    @Autowired
    private lateinit var categoryService: CategoryService
    @Autowired
    private lateinit var sessionService: SessionService
    @Autowired
    private lateinit var shopService: ShopService
    @Autowired
    private lateinit var productService: ProductService

    @PostMapping("/categories", consumes = ["application/json"], produces = ["application/json"])
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

        val user = session.get().user
        if (!shopService.findByUser(user).isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "You don't have shop yet"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (!map.keys.contains("name")) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Required arguments are missing"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val name = if (map["name"] is String) map["name"].toString() else ""

        if (name == "") {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "One or few arguments are empty"),
                    HttpStatus.BAD_REQUEST
            )
        }

        val category = categoryService.save(
                Category(
                        name = name,
                        shop = shopService.findByUser(user).get()
                )
        )

        return ResponseEntity(
                mapOf("error" to false, "category" to category.serialized()),
                HttpStatus.OK
        )
    }

    @GetMapping("/categories/{id}", consumes = ["application/json"], produces = ["application/json"])
    fun info(@PathVariable("id") id: Long): ResponseEntity<Map<String, Any>> {
        val category = categoryService.findById(id)
        if (!category.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Category not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        return ResponseEntity(
                mapOf("error" to false, "category" to category.get().serialized()),
                HttpStatus.OK
        )
    }

    @PutMapping("/categories", consumes = ["application/json"], produces = ["application/json"])
    fun update(@RequestBody map: Map<String, Any>): ResponseEntity<Map<String, Any>> {
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
        val category = categoryService.findById(id)
        if (session.get().user.id != category.get().shop.user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        if (!map.keys.contains("name")) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "At least one argument required"),
                    HttpStatus.BAD_REQUEST
            )
        }

        if (map["name"] is String) {
            category.get().name = map["name"].toString()
        }

        categoryService.save(category.get())

        return ResponseEntity(
                mapOf("error" to false, "category" to category.get().serialized()),
                HttpStatus.OK
        )
    }

    @DeleteMapping("/categories", consumes = ["application/json"], produces = ["application/json"])
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

        val category = categoryService.findById(id)
        if (!category.isPresent) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "Category not found"),
                    HttpStatus.NOT_FOUND
            )
        }

        if (session.get().user.id != category.get().shop.user.id && !session.get().user.roles.contains(Role.SUPER)) {
            return ResponseEntity(
                    mapOf("error" to true, "message" to "No necessary authorities"),
                    HttpStatus.FORBIDDEN
            )
        }

        productService.findAllByCategory(category.get()).forEach {
            it.category = null
        }

        categoryService.deleteById(category.get().id)

        return ResponseEntity(
                mapOf("error" to false),
                HttpStatus.OK
        )
    }

    @PostMapping("/categories/all", consumes = ["application/json"], produces = ["application/json"])
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

        val page = categoryService.findAll(PageRequest(pageNumber, limit))

        return ResponseEntity(
                mapOf("error" to false, "categories" to page.content.serialized(), "total" to page.totalElements),
                HttpStatus.OK
        )
    }

    fun Collection<Category>.serialized() = this.map { it.serialized() }.toList()
}