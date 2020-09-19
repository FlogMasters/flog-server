package com.flogmasters.flog.oauth.repository

import com.flogmasters.flog.oauth.model.entity.AccessToken
import org.springframework.data.jpa.repository.JpaRepository

interface AccessTokenRepository: JpaRepository<AccessToken, Long>{
    fun findByTokenAndConnectedIpAndExpired(token:String,connectedIp:String,expired:Boolean):AccessToken?
    fun findByConnectedIpAndExpired(connectedIp: String, expired: Boolean):AccessToken?
}