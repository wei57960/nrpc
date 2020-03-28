package com.cs.definedserver.protocolhandlers.demo.decoder;


import com.cs.definedserver.protocolhandlers.DeviceManagement;
import com.cs.definedserver.protocolhandlers.demo.entity.DemoEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @Author wei
 * @Time 2020/3/16
 * @Description old 协议
 */
@Slf4j
public class DemoOldEntityDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final byte[] versionPrefix = {0x1e};

    /**
     * 解析成 entity
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        msg.markReaderIndex();
        ByteBuf header = msg.readBytes(1);
        if (Unpooled.copiedBuffer(versionPrefix).equals(header)) {
            msg.skipBytes(1);
            DemoEntity entity = new DemoEntity();
            entity.setDeviceCode(UUID.randomUUID().toString().replace("-", ""));

            byte alarmType = msg.readByte();
            entity.setAlarmType(String.valueOf(alarmType));

            byte deviceType = msg.readByte();
            entity.setDeviceType(String.valueOf(deviceType));

            byte loop = msg.readByte();
            entity.setLoop(String.valueOf(loop));

            byte addressCode = msg.readByte();
            entity.setAddressCode(String.valueOf(addressCode));

            out.add(entity);
            log.debug("old protocol demo entity:[{}]", entity.toString());
            // 进行保存连接
            DeviceManagement.putDeviceChannelMap(entity.getDeviceCode(), ctx.channel());
        } else {
            msg.resetReaderIndex();
            // 否则执行其他 decoder
            ctx.fireChannelRead(msg);
        }
    }

}
