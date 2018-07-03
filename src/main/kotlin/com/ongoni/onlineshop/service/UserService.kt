package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun findById(id: Long): Optional<User> = userRepository.findById(id)

    fun findOneByUsername(username: String): User = userRepository.findOneByUsername(username)

    fun save(user: User) = userRepository.saveAndFlush(user)

}