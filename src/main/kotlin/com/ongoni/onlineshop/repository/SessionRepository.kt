package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Session
import com.ongoni.onlineshop.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SessionRepository : JpaRepository<Session, Long> {

    fun findOneByToken(token: String): Optional<Session>

    fun findAllByUser(user: User): MutableList<Session>

}