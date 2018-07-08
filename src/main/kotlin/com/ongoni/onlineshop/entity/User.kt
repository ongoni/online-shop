package com.ongoni.onlineshop.entity

import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @get:JvmName("getUsername_")
        var username: String = "",

        @get:JvmName("getPassword_")
        var password: String = "",

        var email: String = "",

        @Column(name = "first_name")
        var firstName: String = "",

        @Column(name = "last_name")
        var lastName: String = "",

        @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
        @CollectionTable(name = "role", joinColumns = [(JoinColumn(name = "user_id"))])
        @Enumerated(EnumType.STRING)
        val roles: MutableSet<Role> = mutableSetOf(),

        var active: Boolean = true
) {

    fun safeSerialized(): Map<String, Any> = mapOf(
            "id" to id,
            "username" to username,
            "first_name" to firstName,
            "last_name" to lastName,
            "email" to email,
            "roles" to roles
    )

}