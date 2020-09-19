package com.flogmasters.flog.oauth.controller

import com.flogmasters.flog.common.exception.ResultCode
import com.flogmasters.flog.common.model.User
import com.flogmasters.flog.common.model.response.Response
import com.flogmasters.flog.oauth.exception.FlogAuthException
import com.flogmasters.flog.oauth.model.entity.AccessToken
import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import com.flogmasters.flog.oauth.model.request.IssueTokenRequest
import com.flogmasters.flog.oauth.model.request.LoginRequest
import com.flogmasters.flog.oauth.model.response.AccessTokenResponse
import com.flogmasters.flog.oauth.model.response.AuthorizationCodeResponse
import com.flogmasters.flog.oauth.service.AuthService
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.web.bind.annotation.*
import javax.persistence.Access


@RestController
@RequestMapping("/oauth")
class AuthController(
    private val authService: AuthService
){

    @PostMapping("/user/login")
    fun login(@RequestBody loginRequest: LoginRequest):Response<AuthorizationCodeResponse>{
        val authorizationCode = authService.login(loginRequest)
        return Response(ResultCode.SUCCESS, true, AuthorizationCodeResponse(authorizationCode.authorizationCode))
    }

    @PostMapping("/token")
    fun issueToken(
            @ApiParam(value = "type = authorizationCode, refreshToken 선택")
            @RequestBody issueTokenRequest: IssueTokenRequest):Response<AccessTokenResponse>{
        return Response(ResultCode.SUCCESS, true,authService.makeAccessToken(issueTokenRequest))
    }

    @GetMapping("/token")
    fun findUserForAccessToken(@RequestParam token:String):Response<User>{
        return Response(ResultCode.SUCCESS, true, authService.checkingAccessToken(token))
    }

    @GetMapping("/user/logout")
    fun logout(@RequestHeader("Authorization") token:String):Response<User>{
        val userToken = checkBearerToken(token.toLowerCase())
        return Response(ResultCode.SUCCESS, true, authService.logout(userToken))
    }

    private fun checkBearerToken(token:String):String{
        if(token.startsWith("bearer")){
            return token.replace("bearer","").trim()
        }else
            throw FlogAuthException("not bearer token")

    }
}