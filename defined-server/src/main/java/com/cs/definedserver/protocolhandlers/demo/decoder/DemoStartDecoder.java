package com.cs.definedserver.protocolhandlers.demo.decoder;

import com.cs.definedserver.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

/**
 * @Author wei
 * @Time 2020/3/18
 * @Description 数据拆包 校验 应答
 */
public class DemoStartDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 协议头
     */
    private static final byte[] prefix = {0x53, 0x4e};
    private static final ByteBuf delimiter = Unpooled.copiedBuffer(prefix);
    private static final int maxFrameLength = 300;
    private static final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    private static final int lengthFieldOffset = 2;
    private static final int lengthFieldLength = 2;
    private static final int lengthAdjustment = -4;
    private static final int initialBytesToStrip = 4;
    private static final boolean failFast = true;
    private static final Logger log = LoggerFactory.getLogger(DemoStartDecoder.class);


    /**
     * byteOrder 长度域表示的数据使用大端模式还是小端模式
     * maxFrameLength 包的最大长度
     * lengthFieldOffset 长度域的偏移量
     * lengthFieldLength 长度域长度
     * lengthAdjustment 包体长度调整的大小
     * initialBytesToStrip 向下传递之前跳过多少字节
     * failFast 如果为 true ，则表示读取到长度域，TA的值的超过 maxFrameLength，
     * 就抛出一个 TooLongFrameException，
     * 而为 false 表示只有当真正读取完长度域的值表示的字节之后，
     * 才会抛出 TooLongFrameException，
     * 默认情况下设置为 true ，建议不要修改，否则可能会造成内存溢出
     */
    public DemoStartDecoder() {
        super(byteOrder,
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip,
                failFast);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in.markReaderIndex();

        // check 协议头
        ByteBuf headerByteBuf = in.readBytes(prefix.length);
        if (!headerByteBuf.equals(delimiter)) {
            byte[] bytes = new byte[headerByteBuf.readableBytes()];
            headerByteBuf.readBytes(bytes);
            String instruction = ByteUtil.getByteArrayString(bytes);
            log.error("不支持的协议头: [{}]", instruction);
            ctx.channel().close();
            return null;
        }

        // check 校验和

        // 校验成功 进行应答
        ctx.channel().writeAndFlush(calculateReplayData(ctx, in));


        in.resetReaderIndex();
        return super.decode(ctx, in);
    }

    /**
     * 计算应答数据
     *
     * @param data
     * @return
     */
    private ByteBuf calculateReplayData(ChannelHandlerContext ctx, ByteBuf data) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byte[] replayData = "success".getBytes(CharsetUtil.UTF_8);
        log.debug("应答的数据为：[{}]", ByteUtil.getByteArrayString(replayData));
        byteBuf.writeBytes(replayData);
        return byteBuf;
    }

}
