package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun findOneByUsername(username: String): Optional<User>

    fun findOneByEmail(email: String): Optional<User>

}