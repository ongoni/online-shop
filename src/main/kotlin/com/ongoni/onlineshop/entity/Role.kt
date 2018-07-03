package com.ongoni.onlineshop.entity

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER, SUPER;

    override fun getAuthority(): String {
        return name
    }
}