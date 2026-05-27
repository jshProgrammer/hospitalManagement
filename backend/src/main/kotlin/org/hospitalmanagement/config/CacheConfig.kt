package org.hospitalmanagement.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val caches = listOf(
            build("departments", maxSize = 500, ttlMinutes = 15),
            build("departments-search", maxSize = 200, ttlMinutes = 15),
            build("stations", maxSize = 1000, ttlMinutes = 15),
            build("stations-search", maxSize = 200, ttlMinutes = 15),
            build("drugs", maxSize = 1000, ttlMinutes = 5),
            build("drugs-search", maxSize = 200, ttlMinutes = 5),
            build("rooms", maxSize = 2000, ttlMinutes = 15),
            build("rooms-search", maxSize = 200, ttlMinutes = 15),
            build("rooms-floor", maxSize = 200, ttlMinutes = 15),
            build("doctors", maxSize = 1000, ttlMinutes = 10),
            build("nurses", maxSize = 1000, ttlMinutes = 10),
        )
        return SimpleCacheManager().also { it.setCaches(caches) }
    }

    private fun build(name: String, maxSize: Long, ttlMinutes: Long): CaffeineCache =
        CaffeineCache(
            name,
            Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttlMinutes, TimeUnit.MINUTES)
                .build()
        )
}
