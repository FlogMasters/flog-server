package com.flogmasters.flog.oauth.controller

import com.flogmasters.flog.common.exception.ResultCode
import com.flogmasters.flog.common.model.response.Response
import com.flogmasters.flog.oauth.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/found/id")
class FindController(
        private val userService: UserService
){

    //id 찾기
    @GetMapping
    fun findIdForEmail(@RequestParam email:String):Response<Map<String, Boolean>>{
        // 유효성 검증
        // 이메일 보내기
        val result = userService.sendEmailForFoundId(email)
        return Response(ResultCode.SUCCESS, mapOf("result" to result))
    }
}
