package com.flogmasters.flog.oauth.model.entity

import java.time.ZonedDateTime
import javax.persistence.*


@Entity
//@Table(name = "refresh_token")
class RefreshToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        val userId:String,
        val token:String,
        val expiresIn:Long,
        val lastRefreshToken: String?,
        val createdAt:ZonedDateTime,
        var expired:Boolean
)

