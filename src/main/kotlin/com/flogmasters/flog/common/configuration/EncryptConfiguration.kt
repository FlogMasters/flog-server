package com.flogmasters.flog.common.configuration

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
@EnableEncryptableProperties

class EncryptConfiguration(

) {


    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(environment:Environment):StringEncryptor{
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()
        config.password = environment.getProperty("jasypt.encryptor.password","")
        config.algorithm = "PBEWithMD5ANDDES"
        config.keyObtentionIterations = 1000
        config.poolSize = 1
        config.stringOutputType = "base64"
        encryptor.setConfig(config)
        return encryptor
    }



}