package com.ascendant76.redis.support.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

public class KryoSupport {
    private static final Pool<Kryo> KRYO_POOL =
            new Pool<>(true, false, 16) {
                protected Kryo create() {
                    return new Kryo();
                }
            };

    private static final Pool<Output> KRYO_OUTPUT_POOL =
            new Pool<>(true, false, 16) {
                protected Output create() {
                    return new Output(1024, -1);
                }
            };

    private static final Pool<Input> KRYO_INPUT_POOL =
            new Pool<>(true, false, 16) {
                protected Input create() {
                    return new Input(1024);
                }
            };


    public static <I> byte[] serialize(final I object) {
        Kryo kryo = KRYO_POOL.obtain();
        kryo.register(object.getClass());
        Output output = KRYO_OUTPUT_POOL.obtain();
        kryo.writeObject(output, object);
        byte[] bytes = output.toBytes();
        KRYO_POOL.free(kryo);
        KRYO_OUTPUT_POOL.free(output);
        return bytes;
    }

    public static <O> O deserialize(final byte[] dataStream) {
        Kryo kryo = KRYO_POOL.obtain();
        Input input = KRYO_INPUT_POOL.obtain();
        input.setBuffer(dataStream);
        O deserializedObject = (O) kryo.readClassAndObject(input);
        KRYO_POOL.free(kryo);
        KRYO_INPUT_POOL.free(input);
        return deserializedObject;
    }
}
