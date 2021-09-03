package com.flogmasters.flog.common.model.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.flogmasters.flog.common.exception.ResultCode

class Response<T>(
    @JsonProperty("_code")
    val code:Int,
    @JsonProperty("_message")
    val message:String?,
    val isSuccess:Boolean,
    val data:T
){
    constructor(resultCode:ResultCode, isSuccess: Boolean, data:T): this(resultCode.code, resultCode.defaultMessage, isSuccess, data)
    constructor(resultCode: ResultCode, data: T): this(resultCode, true, data)
}