package com.flogmasters.flog.oauth.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.flogmasters.flog.common.model.User
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn


@Entity
class AuthorizationCode(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id:Long? = null,
        @ManyToOne
        @JoinColumn(name = "userId")
        val user: User,
        @JsonIgnore
        val connectedIp:String,
        val authorizationCode:String,
        val createdAt:ZonedDateTime,
        var expired:Boolean
)