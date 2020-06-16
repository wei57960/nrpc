package com.cs.common.serializer;

public interface NRpcSerializer {

    byte[] serializer(Object object);

    <T> T deSerializer(byte[] byteData);
}
