package com.flogmasters.flog.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.flogmasters.flog.AbstractTest
import com.flogmasters.flog.common.model.User
import com.flogmasters.flog.common.model.response.Response
import com.flogmasters.flog.common.repository.UserRepository
import com.flogmasters.flog.oauth.model.entity.AccessToken
import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import com.flogmasters.flog.oauth.model.entity.RefreshToken
import com.flogmasters.flog.oauth.model.request.IssueTokenRequest
import com.flogmasters.flog.oauth.model.request.LoginRequest
import com.flogmasters.flog.oauth.model.response.AuthorizationCodeResponse
import com.flogmasters.flog.oauth.repository.AccessTokenRepository
import com.flogmasters.flog.oauth.repository.AuthorizationCodeRepository
import com.flogmasters.flog.oauth.repository.RefreshTokenRepository
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AuthControllerTests:AbstractTest(){


    @Autowired
    private lateinit var userRepository:UserRepository

    @Autowired
    private lateinit var accessTokenRepository: AccessTokenRepository

    @Autowired
    private lateinit var authorizationCodeRepository: AuthorizationCodeRepository

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    private val localIp = "127.0.0.1"

    private val user: User= User("123","123","123")

    private val authorizationCode = AuthorizationCode(null,user,"127.0.0.1","1234", ZonedDateTime.now(), false)
    private val refreshToken = RefreshToken(null, user, "1234",6000,"", ZonedDateTime.now(),localIp,false)
    private val accessToken = AccessToken(null, user,"aaa", 10000,refreshToken, ZonedDateTime.now(), localIp,false)

    // TODO: 로그인 만들기 전까지
    @BeforeEach
    fun makeUser(){
        userRepository.save(user)
//        authorizationCodeRepository.save(authorizationCode)
//        refreshTokenRepository.save(refreshToken)
//        accessTokenRepository.save(accessToken)
    }



    @Test
    @DisplayName("로그인")
    @Order(1)
    fun makeAuthorizationCode(){
        val request = createCommonObject("/request/logInRequest.json", object : TypeReference<LoginRequest>(){})

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
        authorizationCodeRepository.deleteAll()

    }

    @Test
    @DisplayName("access token 발급")
    @Order(2)
    fun makeAccessTokenAndRefreshToken(){
        val request = IssueTokenRequest("authorizationCode", "authorizationCode")

        val authorizationCode = AuthorizationCode(null,user, "127.0.0.1",request.token, ZonedDateTime.now(), false)
        authorizationCodeRepository.save(authorizationCode)

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        authorizationCodeRepository.deleteAll()
        accessTokenRepository.deleteAll()
    }

    @Test
    @DisplayName("refresh token을 이용한 access token 발급")
    @Order(3)
    fun makeAccessTokenByRefreshToken(){
        val request = IssueTokenRequest("refreshToken","refreshToken")
        val refreshToken = RefreshToken(null, user, request.token,6000,"", ZonedDateTime.now(),localIp,false)
        refreshTokenRepository.save(refreshToken)
        val result = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        accessTokenRepository.deleteAll()
        refreshTokenRepository.deleteAll()
    }
    @Test
    @DisplayName("액세스 토큰 확인")
    @Order(4)
    fun findAccessToken(){
        val refreshToken = RefreshToken(null, user, "1234",6000,"", ZonedDateTime.now(),localIp,false)
        val accessToken = AccessToken(null, user,"aaa", 10000,refreshToken, ZonedDateTime.now(), localIp,false)
        refreshTokenRepository.save(refreshToken)
        accessTokenRepository.save(accessToken)


        val result = mockMvc.perform(MockMvcRequestBuilders.get("/oauth/token?token=aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        accessTokenRepository.deleteAll()
        refreshTokenRepository.deleteAll()

    }

    @Test
    @DisplayName("로그아웃")
    @Order(5)
    fun logout(){
        val authorizationCode = AuthorizationCode(null,user,"127.0.0.1","1234", ZonedDateTime.now(), false)
        val refreshToken = RefreshToken(null, user, "1234",6000,"", ZonedDateTime.now(),localIp,false)
        val accessToken = AccessToken(null, user,"aaa", 10000,refreshToken, ZonedDateTime.now(), localIp,false)
        authorizationCodeRepository.save(authorizationCode)
        refreshTokenRepository.save(refreshToken)
        accessTokenRepository.save(accessToken)

        val response = createCommonObject("response/logoutResponse.json", object : TypeReference<Response<Map<String, Any>>>(){})

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/oauth/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer ${accessToken.token}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
        val resultPayload = objectMapper.readValue(result.response.contentAsString, object : TypeReference<Response<Map<String, Any>>>(){})
        assertEquals(response.data, resultPayload.data)
    }

    private fun deleteAll(){
        authorizationCodeRepository.deleteAll()
        refreshTokenRepository.deleteAll()
    }

}