package com.flogmasters.flog.oauth.repository

import com.flogmasters.flog.oauth.model.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository:JpaRepository<RefreshToken,Long>{
    fun findByTokenAndExpired(token:String,expired:Boolean):RefreshToken?
}