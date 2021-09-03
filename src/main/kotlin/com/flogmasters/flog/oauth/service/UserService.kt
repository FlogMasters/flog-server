package com.flogmasters.flog.oauth.service

interface UserService{
    fun sendEmailForFoundId(email:String):Boolean
}