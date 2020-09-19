package com.flogmasters.flog

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.servlet.MockMvc
import java.io.IOException

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
class AbstractTest(){

    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = jacksonObjectMapper()

    fun<T> createCommonObject(fileName: String, type: TypeReference<T>): T {
        val resource: Resource = ClassPathResource(fileName)
        return try {
            objectMapper.readValue(resource.file, type)
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }
}