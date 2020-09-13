package com.flogmasters.flog.oauth.repository

import com.flogmasters.flog.oauth.model.entity.AccessToken
import org.springframework.data.jpa.repository.JpaRepository

interface AccessTokenRepository: JpaRepository<AccessToken, Long>{
    fun findByTokenAndExpired(token:String,expired:Boolean):AccessToken?
}