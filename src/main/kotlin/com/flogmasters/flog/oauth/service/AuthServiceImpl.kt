package com.flogmasters.flog.oauth.service

import com.flogmasters.flog.common.exception.FlogException
import com.flogmasters.flog.common.model.User
import com.flogmasters.flog.oauth.model.request.LoginRequest
import com.flogmasters.flog.common.repository.UserRepository
import com.flogmasters.flog.oauth.model.entity.AccessToken
import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import com.flogmasters.flog.oauth.model.entity.RefreshToken
import com.flogmasters.flog.oauth.model.request.IssueTokenRequest
import com.flogmasters.flog.oauth.model.type.TokenType
import com.flogmasters.flog.oauth.repository.AccessTokenRepository
import com.flogmasters.flog.oauth.repository.AuthorizationCodeRepository
import com.flogmasters.flog.oauth.repository.RefreshTokenRepository
import com.flogmasters.flog.oauth.util.SecurityUtil
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
        private val securityUtil:SecurityUtil
):AuthService{

    companion object {
        const val ACCESS_EXPIRES_IN:Long = 3600
        const val REFRESH_EXPIRES_IN:Long = 864000
    }

    override fun login(loginRequest: LoginRequest): AuthorizationCode {
        val user = userRepository.findByIdOrNull(loginRequest.id) ?: throw FlogException("not found user")
        if(user.password != loginRequest.password){
            throw FlogException("password don't correct")
        }
        val code = securityUtil.makeToken(user.id)
        val authorizationCode = AuthorizationCode(null, user.id, "",code,ZonedDateTime.now(),false)
        authorizationCodeRepository.save(authorizationCode)
        return authorizationCode
    }

    override fun makeAccessToken(issueTokenRequest: IssueTokenRequest): AccessToken {
        return when(TokenType[issueTokenRequest.type]){
            TokenType.AUTHORIZATION_CODE -> makeAccessTokenByAuthorizationCode(issueTokenRequest.token)
            TokenType.REFRESH_TOKEN -> makeAccessTokenByRefreshToken(issueTokenRequest.token)
            else -> throw FlogException("error")
        }
    }

    @Transactional
    override fun logout(token:String):User{
        val accessToken = accessTokenRepository.findByTokenAndExpired(token,false) ?: throw FlogException("")
        accessToken.expired = true
        var hasRefreshToken = true
        var refreshToken = accessToken.lastRefreshToken
        //TODO: 개선작업필요
        while(hasRefreshToken){
            val ref = refreshTokenRepository.findByTokenAndExpired(refreshToken, false)
            if(ref != null){
                ref.expired = true
                hasRefreshToken = true
            }
            if(ref?.lastRefreshToken == null){
                break
            }else{
                refreshToken = ref.lastRefreshToken
            }
        }
        val user = userRepository.findByIdOrNull(accessToken.userId) ?: throw FlogException("")
        val authorizationCode = authorizationCodeRepository.findByUserIdAndConnectedIp(user.id,"")
        authorizationCode.expired = true
        return userRepository.findByIdOrNull(accessToken.userId) ?: throw FlogException("")
    }

    override fun checkingAccessToken(token:String):User {
        val accessToken = accessTokenRepository.findByTokenAndExpired(token, false) ?: throw FlogException("not found Token")
        if(isExpiredAccessToken(accessToken)){
            accessToken.expired = true
            accessTokenRepository.save(accessToken)
            throw FlogException("expired token")
        }
        return userRepository.findByIdOrNull(accessToken.userId) ?: throw FlogException("")
    }

    private fun checkAndRemakeRefreshToken(token:String): RefreshToken {
        val refreshToken = refreshTokenRepository.findByTokenAndExpired(token, false) ?: throw FlogException("RefreshToken")
        val expiresTime = refreshToken.createdAt
        val nowTime = ZonedDateTime.now()
        return if(expiresTime.plusSeconds(refreshToken.expiresIn)  < nowTime){
            refreshToken.expired = true
            makeRefreshToken(refreshToken.userId,refreshToken.token, nowTime)
        }else {
            refreshToken
        }
    }
    // TODO:ip가 필요
    private fun makeAccessTokenByAuthorizationCode(token:String):AccessToken{
        val nowTime = ZonedDateTime.now()
        val authorizationCode = authorizationCodeRepository.findByAuthorizationCode(token) ?: throw FlogException("not found authorization_code")
        val refreshToken = makeRefreshToken(authorizationCode.userId,null, nowTime)
        val accessToken = AccessToken(null, authorizationCode.userId,SecurityUtil.makeToken(authorizationCode.userId), ACCESS_EXPIRES_IN,refreshToken.token,nowTime,false)
        return accessTokenRepository.save(accessToken)
    }
    private fun makeAccessTokenByRefreshToken(refreshToken:String):AccessToken{
        val nowTime = ZonedDateTime.now()
        val checkingRefreshToken = checkAndRemakeRefreshToken(refreshToken)
        val accessToken = AccessToken(null, checkingRefreshToken.userId,SecurityUtil.makeToken(checkingRefreshToken.userId), ACCESS_EXPIRES_IN,checkingRefreshToken.token,nowTime,false)
        return accessTokenRepository.save(accessToken)
    }
    private fun makeRefreshToken(userId:String,previousToken:String?, nowTime:ZonedDateTime):RefreshToken {
        val newRefreshToken = RefreshToken(null,userId,SecurityUtil.makeToken(userId),REFRESH_EXPIRES_IN,previousToken, nowTime,false)
        return refreshTokenRepository.save(newRefreshToken)
    }

    private fun isExpiredAccessToken(accessToken:AccessToken):Boolean{
        val time = accessToken.createdAt
        val expiredTime = time.plusSeconds(accessToken.expiresIn)
        return expiredTime <= ZonedDateTime.now()
    }

}