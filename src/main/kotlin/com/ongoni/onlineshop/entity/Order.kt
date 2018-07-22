package com.ongoni.onlineshop.entity

//import com.ongoni.onlineshop.utils.serialized
import com.ongoni.onlineshop.utils.serialized
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_order")
data class Order(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var user: User = User(),

        @ManyToMany
        @JoinTable(name = "order_product",
                joinColumns = [(JoinColumn(name = "order_id"))],
                inverseJoinColumns = [(JoinColumn(name = "product_id"))])
        var products: MutableList<Product> = mutableListOf(),

        var date: Date = Date(),

        @Column(name = "is_paid")
        var isPaid: Boolean = false,

        @Column(name = "is_done")
        var isDone: Boolean = false
) {

    fun serialized(detailed: Boolean = false): Map<String, Any?> {
        val result = mutableMapOf(
                "id" to id,
                "user" to user.serialized(idOnly = !detailed),
                "date" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date),
                "is_paid" to isPaid,
                "is_done" to isDone,
                "products" to products.serialized()
        )
        if (detailed) {
            result["products"] = products.serialized()
        }

        return result.toMap()
    }

}