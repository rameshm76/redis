package com.ascendant76.redis.support.redis;

import com.ascendant76.redis.support.kryo.KryoSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.xerial.snappy.Snappy;

import java.io.IOException;

import static com.ascendant76.redis.support.kryo.KryoSupport.EMPTY_BYTE_ARRAY;

/**
 * Kryo serialization with Snappy compression and uncompress
 *
 * @param <T>
 */
@Slf4j
public class KryoRedisSerializer<T> implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        try {
            if (t == null) return EMPTY_BYTE_ARRAY;
            return Snappy.compress(KryoSupport.serialize(t));
        } catch (IOException e) {
            throw new SerializationException("Snappy compression exception", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            if (bytes == null) return null;
            return KryoSupport.deserialize(Snappy.uncompress(bytes));
        } catch (IOException e) {
            throw new SerializationException("Snappy uncompress exception", e);
        }
    }
}
