package com.flogmasters.flog.oauth.controller

import com.flogmasters.flog.common.exception.ResultCode
import com.flogmasters.flog.common.model.User
import com.flogmasters.flog.common.model.response.Response
import com.flogmasters.flog.oauth.model.entity.AccessToken
import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import com.flogmasters.flog.oauth.model.request.IssueTokenRequest
import com.flogmasters.flog.oauth.model.request.LoginRequest
import com.flogmasters.flog.oauth.service.AuthService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/oauth")
class AuthController(
    private val authService: AuthService
){

    @PostMapping("/user/login")
    fun login(@RequestBody loginRequest: LoginRequest):Response<AuthorizationCode>{
        return Response(ResultCode.SUCCESS, true, authService.login(loginRequest))
    }

    @PostMapping("/token")
    fun issueToken(@RequestBody issueTokenRequest: IssueTokenRequest):Response<AccessToken>{
        return Response(ResultCode.SUCCESS, true, authService.makeAccessToken(issueTokenRequest))
    }

    @GetMapping("/token")
    fun findUserForAccessToken(@RequestParam token:String):Response<User>{
        return Response(ResultCode.SUCCESS, true, authService.checkingAccessToken(token))
    }

    @GetMapping("/logout")
    fun logout(@RequestHeader("Authorization") token:String):Response<User>{
        return Response(ResultCode.SUCCESS, true, authService.logout(token))
    }
}