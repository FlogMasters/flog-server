package com.flogmasters.flog.oauth.service

import com.flogmasters.flog.common.exception.FlogException
import com.flogmasters.flog.common.model.FlogContext
import com.flogmasters.flog.common.model.User
import com.flogmasters.flog.oauth.model.request.LoginRequest
import com.flogmasters.flog.common.repository.UserRepository
import com.flogmasters.flog.oauth.exception.FlogAuthException
import com.flogmasters.flog.oauth.model.entity.AccessToken
import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import com.flogmasters.flog.oauth.model.entity.RefreshToken
import com.flogmasters.flog.oauth.model.request.IssueTokenRequest
import com.flogmasters.flog.oauth.model.response.AccessTokenResponse
import com.flogmasters.flog.oauth.model.type.TokenType
import com.flogmasters.flog.oauth.repository.AccessTokenRepository
import com.flogmasters.flog.oauth.repository.AuthorizationCodeRepository
import com.flogmasters.flog.oauth.repository.RefreshTokenRepository
import com.flogmasters.flog.oauth.util.SecurityUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class AuthServiceImpl(
        private val userRepository: UserRepository,
        private val authorizationCodeRepository: AuthorizationCodeRepository,
        private val refreshTokenRepository: RefreshTokenRepository,
        private val accessTokenRepository: AccessTokenRepository,
        private val securityUtil:SecurityUtil,
        @Value("\${token.access-token.expires}") private val accessTokenExpiresIn:String,
        @Value("\${token.refresh-token.expires}") private val refreshTokenExpiresIn:String
):AuthService{
    // TODO: IP별로 다른 로그인시 구현 필요
    override fun login(loginRequest: LoginRequest): AuthorizationCode {
        val user = userRepository.findByIdOrNull(loginRequest.id) ?: throw FlogAuthException("not found user")
        if(user.password != loginRequest.password){
            throw FlogAuthException("password don't correct")
        }
        val code = securityUtil.makeToken(user.id)
        val ip = FlogContext.getFlogHeader().headerMap?.get("client-ip") ?: throw FlogAuthException("not found host")
        val authorizationCode = AuthorizationCode(null, user,ip,code,ZonedDateTime.now(),false)
        authorizationCodeRepository.save(authorizationCode)
        return authorizationCode
    }

    override fun makeAccessToken(issueTokenRequest: IssueTokenRequest): AccessTokenResponse {
       val accessToken =  when(TokenType[issueTokenRequest.type]){
            TokenType.AUTHORIZATION_CODE -> makeAccessTokenByAuthorizationCode(issueTokenRequest.token)
            TokenType.REFRESH_TOKEN -> makeAccessTokenByRefreshToken(issueTokenRequest.token)
            else -> throw FlogAuthException("not correct grant type")
        }
        return AccessTokenResponse(accessToken.token,
                accessToken.expiresIn,
                accessToken.lastRefreshToken.token,
                accessToken.lastRefreshToken.expiresIn,
                accessToken.createdAt)
    }

    @Transactional
    override fun logout(token:String):User{
        val accessToken = accessTokenRepository.findByTokenAndExpired(token,false) ?: throw FlogAuthException("not found access token")
        accessToken.expired = true
        var hasRefreshToken = true
        var refreshToken = accessToken.lastRefreshToken
        //TODO: 개선작업필요
        while(hasRefreshToken){
            val ref = refreshTokenRepository.findByTokenAndExpired(refreshToken.token, false)
            if(ref != null){
                ref.expired = true
                hasRefreshToken = true
            }
            if(ref?.lastRefreshToken == null){
                break
            }else{
                refreshToken = ref
            }
        }
        val user = userRepository.findByIdOrNull(accessToken.user.id) ?: throw FlogAuthException("not found user")
        val ip = FlogContext.getFlogHeader().headerMap?.get("client-ip") ?: throw FlogAuthException("not found host")
        val authorizationCode = authorizationCodeRepository.findByUserIdAndConnectedIp(user.id,ip)
        authorizationCode.expired = true
        return user
    }

    override fun checkingAccessToken(token:String):User {
        val accessToken = accessTokenRepository.findByTokenAndExpired(token, false) ?: throw FlogAuthException("not found Token")
        if(isExpiredAccessToken(accessToken)){
            accessToken.expired = true
            accessTokenRepository.save(accessToken)
            throw FlogException("expired token")
        }
        return userRepository.findByIdOrNull(accessToken.user.id) ?: throw FlogAuthException("not found token")
    }

    private fun checkAndRemakeRefreshToken(token:String): RefreshToken {
        val refreshToken = refreshTokenRepository.findByTokenAndExpired(token, false) ?: throw FlogAuthException("not found refresh token")
        val expiresTime = refreshToken.createdAt
        val nowTime = ZonedDateTime.now()
        return if(expiresTime.plusSeconds(refreshToken.expiresIn)  < nowTime){
            refreshToken.expired = true
            makeRefreshToken(refreshToken.user,refreshToken.token, nowTime)
        }else {
            refreshToken
        }
    }
    // TODO:ip가 필요
    private fun makeAccessTokenByAuthorizationCode(token:String):AccessToken{
        val nowTime = ZonedDateTime.now()
        val authorizationCode = authorizationCodeRepository.findByAuthorizationCode(token) ?: throw FlogAuthException("not found authorization code")
        val refreshToken = makeRefreshToken(authorizationCode.user,null, nowTime)
        val accessToken = AccessToken(null, authorizationCode.user,SecurityUtil.makeToken(authorizationCode.user.id), accessTokenExpiresIn.toLong(),refreshToken,nowTime,false)
        return accessTokenRepository.save(accessToken)
    }
    private fun makeAccessTokenByRefreshToken(refreshToken:String):AccessToken{
        val nowTime = ZonedDateTime.now()
        val checkingRefreshToken = checkAndRemakeRefreshToken(refreshToken)
        val accessToken = AccessToken(null, checkingRefreshToken.user,SecurityUtil.makeToken(checkingRefreshToken.user.id), accessTokenExpiresIn.toLong(),checkingRefreshToken,nowTime,false)
        return accessTokenRepository.save(accessToken)
    }
    private fun makeRefreshToken(user:User,previousToken:String?, nowTime:ZonedDateTime):RefreshToken {
        val newRefreshToken = RefreshToken(null,user,SecurityUtil.makeToken(user.id),refreshTokenExpiresIn.toLong(),previousToken, nowTime,false)
        return refreshTokenRepository.save(newRefreshToken)
    }

    private fun isExpiredAccessToken(accessToken:AccessToken):Boolean{
        val time = accessToken.createdAt
        val expiredTime = time.plusSeconds(accessToken.expiresIn)
        return expiredTime <= ZonedDateTime.now()
    }

}
