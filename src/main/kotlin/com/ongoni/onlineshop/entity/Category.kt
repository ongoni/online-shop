package com.ongoni.onlineshop.entity

import javax.persistence.*

@Entity
@Table(name = "category")
data class Category(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,


        @Column(nullable = false)
        var name: String = "",

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "shop_id")
        var shop: Shop = Shop()
) {

    fun serialized() = mapOf(
            "id" to id,
            "name" to name
    )

}