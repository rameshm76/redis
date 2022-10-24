package com.ascendant76.redis.support.kryo;

import com.ascendant76.redis.entities.Account;
import com.ascendant76.redis.entities.Order;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;

@Slf4j
public class KryoSupport {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static final Pool<Kryo> KRYO_POOL = new Pool<>(true, false, 16) {
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.register(Account.class);
            kryo.register(Order.class);
            return kryo;
        }
    };

    public static <T> byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }

        Kryo kryo = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            kryo = KRYO_POOL.obtain();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(false);

            Output output = new Output(baos);
            kryo.writeClassAndObject(output, t);
            output.flush();
            return baos.toByteArray();
        } finally {
            KRYO_POOL.free(kryo);
        }
    }

    public static <T> T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        Kryo kryo = null;
        try {
            kryo = KRYO_POOL.obtain();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(false);
            try (Input input = new Input(bytes)) {
                return (T) kryo.readClassAndObject(input);
            }
        } finally {
            KRYO_POOL.free(kryo);
        }
    }
}
