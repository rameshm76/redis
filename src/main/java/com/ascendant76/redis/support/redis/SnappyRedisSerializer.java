package com.ascendant76.redis.support.redis;

import com.ascendant76.redis.AppRuntimeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class SnappyRedisSerializer<T> implements RedisSerializer<T> {

    private RedisSerializer<T> redisSerializer;

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            LOG.info("T {}", t);
            return Snappy.compress(Objects.requireNonNull(redisSerializer.serialize(t)));
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            byte[] uncompressedBytes = Snappy.uncompress(bytes);
            return redisSerializer.deserialize(uncompressedBytes);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }
}
