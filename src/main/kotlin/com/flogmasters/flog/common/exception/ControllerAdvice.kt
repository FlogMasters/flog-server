package com.flogmasters.flog.common.exception

import com.flogmasters.flog.common.model.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ControllerAdvice{


    @ExceptionHandler(Exception::class)
    private fun handleVisionRunTimeException(exception: Exception): ResponseEntity<Response<*>>{
        return makeResponseException(exception)
    }

    private fun makeResponseException(exception: Exception): ResponseEntity<Response<*>>{
        return when(exception){
            is FlogException -> {
                makeResponse(exception.message, exception.resultCode, exception.cause)
            }
            else -> {
                makeResponse(exception.message, ResultCode.INTERNAL_SERVER_ERROR, mapOf("message" to exception.message))
            }
        }
    }

    private fun makeResponse(message:String?, resultCode:ResultCode, data:Any? = null):ResponseEntity<Response<*>>{
        return ResponseEntity(Response(resultCode.code,message, false, data), resultCode.httpStatus)
    }


}