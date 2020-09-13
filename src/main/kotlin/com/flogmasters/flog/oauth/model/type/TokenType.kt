package com.flogmasters.flog.oauth.model.type

enum class TokenType(
        val type:String
){
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    companion object{
        private val LOOK_UP:MutableMap<String, TokenType> = mutableMapOf()
        operator fun get(type: String): TokenType? = LOOK_UP[type]
        init {
            values().map { LOOK_UP[it.type] = it }
        }
    }
}