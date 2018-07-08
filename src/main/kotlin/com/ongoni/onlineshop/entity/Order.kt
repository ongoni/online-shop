package com.ongoni.onlineshop.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_order")
data class Order(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user")
        var user: User = User(),

        @ManyToMany
        @JoinTable(name = "order_product",
                joinColumns = [(JoinColumn(name = "shop_id"))],
                inverseJoinColumns = [(JoinColumn(name = "product_id"))])
        var products: MutableList<Product> = mutableListOf(),

        var date: Date = Date(),

        @Column(name = "is_paid")
        var isPaid: Boolean = false,

        @Column(name = "is_done")
        var isDone: Boolean = false
)