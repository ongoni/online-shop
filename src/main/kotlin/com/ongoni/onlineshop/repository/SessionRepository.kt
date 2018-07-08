package com.ongoni.onlineshop.repository

import com.ongoni.onlineshop.entity.Session
import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<Session, Long> {
}