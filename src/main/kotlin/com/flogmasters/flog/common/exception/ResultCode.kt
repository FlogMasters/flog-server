package com.flogmasters.flog.common.exception

import org.springframework.http.HttpStatus

enum class ResultCode(
        val httpStatus: HttpStatus,
        val code:Int,
        val defaultMessage:String
){
    SUCCESS(HttpStatus.OK, 200_0000, "SUCCESS"),
    FAIL(HttpStatus.OK, 200_0001, "FAIL"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400_0000, "Bad Request"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500_0000, "internal_server_error")
}