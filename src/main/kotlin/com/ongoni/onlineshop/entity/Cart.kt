package com.ongoni.onlineshop.entity

import com.ongoni.onlineshop.utils.serialized
import javax.persistence.*

@Entity
@Table(name = "cart")
data class Cart(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @OneToOne(fetch = FetchType.LAZY)
        @MapsId
        var user: User = User(),

        @ManyToMany
        @JoinTable(name = "cart_product",
                joinColumns = [(JoinColumn(name = "cart_id"))],
                inverseJoinColumns = [(JoinColumn(name = "product_id"))])
        var products: MutableList<Product> = mutableListOf()
) {

    fun serialized() = mapOf(
            "id" to id,
            "user" to user.serialized(),
            "products" to products.serialized()
    )
}