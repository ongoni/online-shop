package com.ongoni.onlineshop.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "shop")
data class Shop(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        var name: String = "",

        var description: String = "",

        @OneToOne(fetch = FetchType.LAZY)
        @MapsId
        var user: User = User(),

        @ManyToMany
        @JoinTable(name = "shop_product",
                joinColumns = [JoinColumn(name = "shop_id")],
                inverseJoinColumns = [(JoinColumn(name = "product_id"))])
        var products: MutableList<Product> = mutableListOf(),

        @Column(name = "open_date")
        var openDate: Date = Date()
)