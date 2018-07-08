package com.ongoni.onlineshop.entity

import javax.persistence.*

@Entity
@Table(name = "product")
data class Product(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(unique = true)
        var name: String = "",

        var description: String = "",

        var amount: Int = 0,

        var price: Double = 0.0
)