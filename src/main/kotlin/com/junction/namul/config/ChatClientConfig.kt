package com.junction.namul.config

import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig(
    @Value("\${upstage.api-key}") private val apiKey: String,
    @Value("\${upstage.base-url}") private val baseUrl: String,
) {

    @Bean
    fun openAiApi(): OpenAiApi {
        return OpenAiApi.builder()
            .baseUrl(baseUrl)
            .apiKey(apiKey)
            .build()
    }

    @Bean
    fun openAiChatModel(openAiApi: OpenAiApi): OpenAiChatModel {
        val options = OpenAiChatOptions.builder()
            .model("solar-pro2")
            .temperature(0.7)
            .build()

        return OpenAiChatModel(openAiApi, options)
    }

}
