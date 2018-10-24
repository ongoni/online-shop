package com.ongoni.onlineshop.service

import com.ongoni.onlineshop.entity.Session
import com.ongoni.onlineshop.entity.User
import com.ongoni.onlineshop.repository.SessionRepository
import com.ongoni.onlineshop.utils.generateToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService {
    @Autowired
    private lateinit var sessionRepository: SessionRepository

    fun findById(id: Long)= sessionRepository.findById(id)

    fun findByToken(token: String) = sessionRepository.findOneByToken(token)

    fun findAllByUser(user: User) = sessionRepository.findAllByUser(user)

    fun save(session: Session) = sessionRepository.save(session)

    fun deleteById(id: Long) = sessionRepository.deleteById(id)

    fun create(user: User): Session {
        val createDate = Date()
        val cal = Calendar.getInstance()
        cal.time = createDate
        cal.add(Calendar.MONTH, 1)

        return Session(
                user = user,
                token = generateToken(),
                createDate = createDate,
                expireDate = cal.time
        )
    }

}