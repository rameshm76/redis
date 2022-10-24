package com.ascendant76.redis.config;

import com.ascendant76.redis.entities.Account;
import com.ascendant76.redis.entities.Order;
import com.ascendant76.redis.support.kryo.KryoSupport;
import com.ascendant76.redis.support.redis.KryoRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.core.convert.RedisConverter;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableRedisRepositories(
        basePackages = "com.ascendant76.redis.repository",
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
public class RedisConfig {

    @Bean("redisConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration("localhost", 6379)
        );
    }

    /**
     * RedisConverter is required  for the spring data redis work with different serializer
     */
    @Bean
    public RedisConverter redisConverter() {
        final MappingRedisConverter redisConverter = new MappingRedisConverter(null, null, null);
        final List<Object> converters = Arrays.asList(
                new Converter<Account, byte[]>() {
                    @Override
                    public byte[] convert(Account account) {
                        return KryoSupport.serialize(account);
                    }
                },
                new Converter<byte[], Account>() {
                    @Override
                    public Account convert(byte[] input) {
                        return KryoSupport.deserialize(input);
                    }
                },
                new Converter<Order, byte[]>() {
                    @Override
                    public byte[] convert(Order order) {
                        return KryoSupport.serialize(order);
                    }
                },
                new Converter<byte[], Order>() {
                    @Override
                    public Order convert(byte[] input) {
                        return KryoSupport.deserialize(input);
                    }
                }
        );
        redisConverter.setCustomConversions(new RedisCustomConversions(converters));
        return redisConverter;
    }


    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            StringRedisSerializer stringRedisSerializer,
            KryoRedisSerializer<Object> kryoRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(kryoRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(kryoRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public <T> KryoRedisSerializer<T> kryoRedisSerializer() {
        return new KryoRedisSerializer<T>();
    }
}
