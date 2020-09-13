package com.flogmasters.flog.oauth.exception

import com.flogmasters.flog.common.exception.FlogException
import com.flogmasters.flog.common.exception.ResultCode

class FlogAuthException(
        override val resultCode:ResultCode,
        override val message:String,
        override val cause: Throwable?
):FlogException(resultCode,message, cause){

    companion object {
        val defaultResultCode = ResultCode.AUTHORIZATION_ERROR
    }
    constructor(message:String,cause: Throwable?):this(defaultResultCode,message, cause)
    constructor(message:String):this(defaultResultCode,message,null)
}