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

    fun findOneByUsername(username: String): Optional<User> = userRepository.findOneByUsername(username)

    fun findOneByEmail(email: String): Optional<User> = userRepository.findOneByEmail(email)

    fun save(user: User) = userRepository.saveAndFlush(user)

    fun delete(user: User) = userRepository.delete(user)

    fun deleteById(id: Long) = userRepository.deleteById(id)

}