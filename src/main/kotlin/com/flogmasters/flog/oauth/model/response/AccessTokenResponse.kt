package com.flogmasters.flog.oauth.model.response

import java.time.ZonedDateTime

class AccessTokenResponse(
        val accessToken: String,
        val accessTokenExpiresIn:Long,
        val refreshToken:String?,
        val refreshTokenExpiresIn:Long?,
        val createdAt:ZonedDateTime
)