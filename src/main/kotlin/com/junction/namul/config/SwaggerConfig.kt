package com.junction.namul.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Namul API")
                    .description("Namul API Documentation")
                    .version("1.0.0")
            )
            .servers(
                listOf(
                    Server().url("https://namul-backend-production-e240.up.railway.app")
                )
            )
    }
}