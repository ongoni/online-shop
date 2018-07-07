package com.ongoni.onlineshop.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "user")
class User(
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
) : UserDetails {

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles

    override fun isEnabled(): Boolean = active

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = active

    override fun isAccountNonExpired(): Boolean = true

    fun getFullName(): String = "$firstName $lastName"
}