package com.flogmasters.flog.oauth.service

import com.flogmasters.flog.common.model.User
import com.flogmasters.flog.oauth.model.entity.AuthorizationCode
import com.flogmasters.flog.oauth.model.request.IssueTokenRequest
import com.flogmasters.flog.oauth.model.request.LoginRequest
import com.flogmasters.flog.oauth.model.response.AccessTokenResponse

interface AuthService {
    fun login(loginRequest: LoginRequest):AuthorizationCode
    fun makeAccessToken(issueTokenRequest: IssueTokenRequest):AccessTokenResponse
    fun logout(token:String):User
    fun checkingAccessToken(token:String):User
}