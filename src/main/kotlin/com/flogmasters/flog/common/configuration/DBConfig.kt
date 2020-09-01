package com.flogmasters.flog.common.configuration

import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@Profile("stage","production")
class DBConfig(
        @Value("\${spring.datasource.password:1234}") private val dbPassword:String,
        @Value("\${spring.datasource.username:root}") private val username:String,
        @Value("\${spring.datasource.url:localhost}") private val dbHost:String
){
    companion object {
        const val DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"
    }
    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
                .username(username)
                .password(dbPassword)
                .url(dbHost)
                .driverClassName(DRIVER_CLASS_NAME)
                .build()
    }

//    @Bean
//    fun entityMangerFactory(builder:EntityManagerFactory, )
}