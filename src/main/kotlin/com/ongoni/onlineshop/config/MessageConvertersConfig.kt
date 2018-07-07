package com.ongoni.onlineshop.config

import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter

@Configuration
class MessageConvertersConfig {

    fun converters(): HttpMessageConverters = HttpMessageConverters(
            true,
            listOf(GsonHttpMessageConverter())
    )

}