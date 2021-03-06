package com.flogmasters.flog.oauth.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.flogmasters.flog.common.model.User
import java.time.ZonedDateTime
import javax.persistence.*


@Entity
//@Table(name = "refresh_token")
class RefreshToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        @ManyToOne
        @JoinColumn(name = "userId")
        val user:User,
        val token:String,
        val expiresIn:Long,
        @JsonIgnore
        val lastRefreshToken: String?,
        val createdAt:ZonedDateTime,
        val connectedIp:String,
        @JsonIgnore
        var expired:Boolean
)

