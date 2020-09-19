package com.flogmasters.flog.common.interceptor

import com.flogmasters.flog.common.exception.FlogException
import com.flogmasters.flog.common.model.FlogContext
import com.flogmasters.flog.common.model.FlogHeader
import com.flogmasters.flog.oauth.util.IPUtil
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class FlogInterceptor : HandlerInterceptorAdapter() {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val headerMap: MutableMap<String, String> = HashMap()
        request.headerNames.toList().map { headerMap[it.toLowerCase()] = request.getHeader(it) }

        val ip = IPUtil.getClientIP(request) ?: throw FlogException("ip error")
        headerMap["client-ip"] = ip
        val header = FlogHeader(headerMap)
        FlogContext.setFlogHeader(header)
        return super.preHandle(request, response, handler)
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        FlogContext.removeAll()
        super.postHandle(request, response, handler, modelAndView)
    }
}