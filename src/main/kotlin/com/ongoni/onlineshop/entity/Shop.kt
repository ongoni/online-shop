package com.ongoni.onlineshop.entity

import com.ongoni.onlineshop.utils.serialized
import java.text.SimpleDateFormat
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

        @OneToOne(fetch = FetchType.LAZY)
        @MapsId
        var user: User = User(),

        @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
        var products: MutableList<Product> = mutableListOf(),

        @Column(name = "open_date")
        var openDate: Date = Date()
) {

    fun serialized(detailed: Boolean = false, idOnly: Boolean = false): Map<String, Any?> = if (idOnly) {
        mapOf("id" to id)
    } else {
        val result = mutableMapOf(
                "id" to id,
                "name" to name,
                "description" to description,
                "owner" to user.serialized(),
                "open_date" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(openDate)
        )
        if (detailed) {
            result["products"] = products.serialized(detailed = false)
        }

        result.toMap()
    }

}