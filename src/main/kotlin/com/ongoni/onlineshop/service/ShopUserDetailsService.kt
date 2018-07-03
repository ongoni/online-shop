package com.ongoni.onlineshop.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class ShopUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var userService: UserService

    override fun loadUserByUsername(username: String): UserDetails = userService.findOneByUsername(username)

}