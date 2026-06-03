package org.hospitalmanagement.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val rateLimitingInterceptor: RateLimitingInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        // Schutz gegen Wörterbuch-Attacken auf Blind-Indizes (Namen, PLZ, Geburtsdatum)
        registry.addInterceptor(rateLimitingInterceptor)
            .addPathPatterns("/api/patients", "/api/diagnoses", "/api/doctors", "/api/nurses")
    }
}
