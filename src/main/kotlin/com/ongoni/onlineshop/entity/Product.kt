package com.ongoni.onlineshop.entity

import javax.persistence.*

@Entity
@Table(name = "product")
data class Product(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        var name: String = "",

        var description: String = "",

        var amount: Int = 0,

        var price: Double = 0.0
)