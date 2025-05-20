package com.example.toygry.webfluxstudy.Board.config

import com.example.toygry.webfluxstudy.Board.controller.BoardResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun redisBoardTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, BoardResponse> {
        val serializer = Jackson2JsonRedisSerializer(BoardResponse::class.java)

        val context = RedisSerializationContext
            .newSerializationContext<String, BoardResponse>(StringRedisSerializer())
            .value(serializer)
            .build()

        return ReactiveRedisTemplate(factory, context)
    }
}