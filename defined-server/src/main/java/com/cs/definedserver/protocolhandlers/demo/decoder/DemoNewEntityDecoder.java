package com.cs.definedserver.protocolhandlers.demo.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Author wei
 * @Time 2020/3/22
 * @Description new 新协议
 */
public class DemoNewEntityDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readableBytes() > 0) {
            // check version 字节 是否符合处理条件

        } else {
            // 跳过此协议解析
            super.channelRead(ctx, msg);
        }

    }

}
