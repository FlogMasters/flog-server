package com.flogmasters.flog.oauth.util

import org.springframework.stereotype.Component
import org.springframework.util.DigestUtils
import java.math.BigInteger
import java.security.MessageDigest
import java.time.ZonedDateTime
import java.util.*
import kotlin.random.Random

@Component
object SecurityUtil{

    fun makeToken(prefix:String):String{
        val md:MessageDigest = MessageDigest.getInstance("SHA-256")
        val code = prefix + makeSalt() +ZonedDateTime.now().toInstant().toEpochMilli()
        md.update(code.toByteArray())
        return String.format("%064x", BigInteger(1, md.digest()))
    }

    private fun makeSalt():String{
        val random = Random.nextBytes(16)
        return String(Base64.getEncoder().encode(random))
    }

}