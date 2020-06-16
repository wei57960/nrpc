package com.cs.common.clinet.initializer;

import com.cs.common.serializer.kryo.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Author wei
 * @Time 2020/6/10
 * @Description
 */
public class ClientDecoder extends LengthFieldBasedFrameDecoder {

    private static final KryoSerializer kryoSerializer = KryoSerializer.getKryoSerializerInstance();

    private static final int MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    private static final int LENGTH_FIELD_LENGTH = 4;

    public ClientDecoder() {
        super(MAX_FRAME_LENGTH,
                0,
                LENGTH_FIELD_LENGTH,
                0,
                LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, in);
        if (buf != null) {
            int length = buf.readableBytes();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);

            return kryoSerializer.deSerializer(bytes);
        }
        return null;
    }
}
