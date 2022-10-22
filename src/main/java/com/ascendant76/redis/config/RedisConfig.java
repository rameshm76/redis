package com.ascendant76.redis.config;

import com.ascendant76.redis.support.redis.KryoRedisSerializer;
import com.ascendant76.redis.support.redis.SnappyRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisCacheConfiguration redisConfiguration(RedisSerializer<Object> snappyRedisSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableKeyPrefix()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(24))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(snappyRedisSerializer));
    }

    @Bean
    public RedisCacheManager redisCacheManager(
            RedisCacheConfiguration redisCacheConfiguration,
            RedisConnectionFactory redisConnectionFactory
    ) {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .enableStatistics()
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer<Object> snappyRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setHashValueSerializer(snappyRedisSerializer);
        return template;
    }

    @Bean
    public RedisSerializer<Object> snappyRedisSerializer() {
        return new SnappyRedisSerializer<>(new KryoRedisSerializer<>());
    }
}
