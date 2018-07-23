package com.ongoni.onlineshop.entity

import javax.persistence.*

@Entity
@Table(name = "product")
data class Product(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        var name: String = "",

        @Column(nullable = false)
        var description: String = "",

        @Column(nullable = false)
        var amount: Int = 0,

        @Column(nullable = false)
        var price: Double = 0.0,

        @ManyToOne(cascade = [CascadeType.DETACH])
        var category: Category? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "shop_id")
        var shop: Shop = Shop()
) {

    fun serialized(detailed: Boolean = false, idOnly: Boolean = false) = if (idOnly) {
        mapOf("id" to id)
    } else {
        mapOf(
                "id" to id,
                "name" to name,
                "description" to description,
                "amount" to amount,
                "price" to price,
                "category" to if (category != null) category!!.serialized() else "null",
                "shop" to shop.serialized(idOnly = !detailed)
        )
    }

}