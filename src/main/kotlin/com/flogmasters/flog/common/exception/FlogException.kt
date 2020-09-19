package com.flogmasters.flog.common.exception

import java.lang.RuntimeException

open class FlogException(
        open val resultCode:ResultCode,
        override val message:String,
        override val cause:Throwable?
):RuntimeException(message,cause){
    companion object {
        val defaultResultCode = ResultCode.INTERNAL_SERVER_ERROR
    }

    constructor(resultCode:ResultCode,message:String): this(resultCode, message, null){

    }

    constructor(resultCode:ResultCode): this(resultCode, resultCode.defaultMessage, null){

    }

    constructor(message:String):this(defaultResultCode, message,null){

    }


}