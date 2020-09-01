package com.flogmasters.flog.common.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HealthController{
    @Value("\${spring.profiles.active:production}")
    private val phase: String? = null

    /**
     * health 반환
     * @return ResponseEntity
     */
    @get:ResponseBody
    @get:GetMapping(value = ["/health_check.html"])
    val ratingStatus: ResponseEntity<String>
        get() = ResponseEntity("flog app-server started!! phase:$phase", HttpStatus.OK)


    @get:GetMapping(value = ["/"])
    val home: String
        get() = "redirect:/health_check.html"

}