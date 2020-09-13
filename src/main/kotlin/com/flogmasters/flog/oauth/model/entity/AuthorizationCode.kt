package com.flogmasters.flog.oauth.model.entity

import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class AuthorizationCode(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        val userId:String,
        val connectedIp:String,
        val authorizationCode:String,
        val createdAt:ZonedDateTime,
        var expired:Boolean
)