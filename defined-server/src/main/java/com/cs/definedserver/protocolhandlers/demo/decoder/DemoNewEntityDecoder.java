package com.cs.definedserver.protocolhandlers.demo.decoder;

import com.cs.definedserver.protocolhandlers.DeviceManagement;
import com.cs.definedserver.protocolhandlers.demo.entity.DemoEntity;
import com.cs.definedserver.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @Author wei
 * @Time 2020/3/22
 * @Description new 新协议
 */
@Slf4j
public class DemoNewEntityDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final byte[] versionPrefix = {0x2e};

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (!msg.isReadable(1)) {
            return;
        }
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
            log.debug("new protocol demo entity:[{}]", entity.toString());
            // 进行保存连接
            DeviceManagement.putDeviceChannelMap(entity.getDeviceCode(), ctx.channel());
            msg.retain();
        } else {
            log.warn("两版本协议均不支持 [{}]", ByteUtil.getByteArrayString(msg.array()));
            ctx.channel().close();
        }
    }

}
