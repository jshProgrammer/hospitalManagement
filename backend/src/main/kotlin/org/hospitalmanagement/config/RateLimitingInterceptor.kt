package org.hospitalmanagement.config

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Component
class RateLimitingInterceptor : HandlerInterceptor {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val bucketKey = "${request.remoteAddr}:${request.servletPath}"
        val bucket = buckets.computeIfAbsent(bucketKey) { createBucket() }

        if (bucket.tryConsume(1)) return true

        response.status = HttpStatus.TOO_MANY_REQUESTS.value()
        response.contentType = "application/json"
        response.writer.write("""{"error":"Zu viele Suchanfragen. Maximal 10 Anfragen pro Minute pro IP erlaubt."}""")
        return false
    }

    private fun createBucket(): Bucket =
        Bucket.builder()
            .addLimit(
                Bandwidth.builder()
                    .capacity(10)
                    .refillGreedy(10, Duration.ofMinutes(1))
                    .build()
            )
            .build()
}
