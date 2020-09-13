package com.flogmasters.flog.oauth.model.entity

import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class AccessToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        val userId:String,
        val token:String,
        val expiresIn:Long,
        val lastRefreshToken:String,
        val createdAt:ZonedDateTime,
        var expired:Boolean
)