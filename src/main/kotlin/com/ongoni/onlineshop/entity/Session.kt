package com.ongoni.onlineshop.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_session")
data class Session(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    var user: User = User(),

    var token: String = "",

    @Column(name = "create_date")
    var createDate: Date = Date(),

    @Column(name = "expire_date")
    var expireDate: Date = Date()

)