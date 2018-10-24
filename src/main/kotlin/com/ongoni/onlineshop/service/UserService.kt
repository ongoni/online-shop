package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun findById(id: Long) = userRepository.findById(id)

    fun findOneByUsername(username: String) = userRepository.findOneByUsername(username)

    fun findOneByEmail(email: String) = userRepository.findOneByEmail(email)

    fun findAll(): MutableList<User> = userRepository.findAll()

    fun findAll(pageable: Pageable) = userRepository.findAll(pageable)

    fun save(user: User) = userRepository.saveAndFlush(user)

    fun deleteById(id: Long) = userRepository.deleteById(id)

}