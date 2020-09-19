package com.flogmasters.flog.oauth.repository

import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorizationCodeRepository:JpaRepository<AuthorizationCode, Long>{
    fun findByAuthorizationCode(authorizationCode: String):AuthorizationCode?
    fun findByUserIdAndConnectedIp(userId:String, connectedIp:String):AuthorizationCode
}