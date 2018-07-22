package com.ongoni.onlineshop.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_session")
data class Session(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    var user: User = User(),

    var token: String = "",

    @Column(name = "create_date")
    var createDate: Date = Date(),

    @Column(name = "expire_date")
    var expireDate: Date = Date()

)