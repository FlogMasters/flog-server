package com.flogmasters.flog.oauth.model.entity

import com.flogmasters.flog.common.model.User
import java.time.ZonedDateTime
import javax.persistence.*


@Entity
class AuthorizationCode(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        @ManyToOne
        @JoinColumn(name = "userId")
        val user: User,
        val connectedIp:String,
        val authorizationCode:String,
        val createdAt:ZonedDateTime,
        var expired:Boolean
)