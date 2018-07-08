package com.ongoni.onlineshop.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "shop")
data class Shop(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(unique = true)
        var name: String = "",

        var description: String = "",

        @OnDelete(action = OnDeleteAction.CASCADE)
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