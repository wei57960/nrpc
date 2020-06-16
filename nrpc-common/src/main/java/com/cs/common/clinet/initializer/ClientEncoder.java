package com.cs.common.clinet.initializer;

import com.cs.common.serializer.kryo.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author wei
 * @Time 2020/6/10
 * @Description serializer
 */
public class ClientEncoder extends MessageToByteEncoder<Object> {

    // todo more type serializer how to choose
    KryoSerializer kryoSerializer = KryoSerializer.getKryoSerializerInstance();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] response = kryoSerializer.serializer(msg);
        int length = response.length;

        out.writeInt(length);
        out.writeBytes(response);
    }

}
