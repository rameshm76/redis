package com.ascendant76.redis.support.redis;

import com.ascendant76.redis.support.kryo.KryoSupport;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class KryoRedisSerializer<T> implements RedisSerializer<T> {
    @Override
    public byte[] serialize(T t) throws SerializationException {
        return KryoSupport.serialize(t);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return KryoSupport.deserialize(bytes);
    }
}
