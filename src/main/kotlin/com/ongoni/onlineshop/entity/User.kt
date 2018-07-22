package com.ongoni.onlineshop.entity

import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(unique = true)
        var username: String = "",

        var password: String = "",

        @Column(unique = true)
        var email: String = "",

        @Column(name = "first_name")
        var firstName: String = "",

        @Column(name = "last_name")
        var lastName: String = "",

        @Column(name = "is_shop_owner")
        var isShopOwner: Boolean = false,

        @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
        @CollectionTable(name = "role", joinColumns = [(JoinColumn(name = "user_id"))])
        @Enumerated(EnumType.STRING)
        var roles: MutableSet<Role> = mutableSetOf(),

        var active: Boolean = true
) {

    fun serialized(idOnly: Boolean = false) = if (idOnly) {
        mapOf("id" to id)
    } else {
        mapOf(
                "id" to id,
                "username" to username,
                "email" to email,
                "first_name" to firstName,
                "last_name" to lastName,
                "is_shop_owner" to isShopOwner,
                "roles" to roles
        )
    }
}