package com.cs.common.serializer.kryo;

import com.cs.common.serializer.NRpcSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author wei
 * @Time 2020/6/10
 * @Description serializer by kryo
 */
public class KryoSerializer implements NRpcSerializer {

    // todo why 1024 手动拆包？
    private static final int SIZE = 1024;

    private static volatile KryoSerializer kryoSerializerInstance;

    private KryoSerializer() {
    }

    public static KryoSerializer getKryoSerializerInstance() {
        if (kryoSerializerInstance == null) {
            synchronized (KryoSerializer.class) {
                if (kryoSerializerInstance == null) {
                    kryoSerializerInstance = new KryoSerializer();
                }
            }
        }
        return kryoSerializerInstance;
    }

    // todo why bio
    @Override
    public byte[] serializer(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(SIZE);
        Output output = new Output(byteArrayOutputStream);

        Kryo kryoSerializer = new Kryo();
        kryoSerializer.writeClassAndObject(output, object);
        output.close();

        byte[] serializerBytes = byteArrayOutputStream.toByteArray();

        return serializerBytes;
    }

    @Override
    public <T> T deSerializer(byte[] byteData) {
        Kryo deSerializerKryo = new Kryo();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteData);
        Input input = new Input(byteArrayInputStream);
        input.close();


        return (T) deSerializerKryo.readClassAndObject(input);
    }
}
