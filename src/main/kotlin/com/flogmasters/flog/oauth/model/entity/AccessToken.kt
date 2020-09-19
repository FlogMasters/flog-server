package com.flogmasters.flog.oauth.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.flogmasters.flog.common.model.User
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class AccessToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        @ManyToOne
        @JoinColumn(name = "userId")
        val user: User,
        val token:String,
        val expiresIn:Long,
        @ManyToOne
        @JoinColumn(name = "lastRefreshToken")
        val lastRefreshToken:RefreshToken,
        val createdAt:ZonedDateTime,
        val connectedIp:String,
        var expired:Boolean
)