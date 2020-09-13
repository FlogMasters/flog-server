package com.flogmasters.flog.common.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig{

    private fun apiInfo():ApiInfo{
        return ApiInfoBuilder().title("flog")
                .description("flog swagger")
                .build()
    }
    private fun globalResponseMessage():List<ResponseMessage>{
        return mutableListOf()
    }

    @Bean
    fun commonApi():Docket{
        return Docket(DocumentationType.SWAGGER_2)
                .groupName("flog")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,globalResponseMessage())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.flogmasters.flog"))
                .paths(PathSelectors.any())
                .build()
    }
}