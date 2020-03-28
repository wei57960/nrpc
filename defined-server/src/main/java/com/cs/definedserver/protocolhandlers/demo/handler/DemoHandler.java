package com.cs.definedserver.protocolhandlers.demo.handler;


import com.cs.definedserver.protocolhandlers.DeviceManagement;
import com.cs.definedserver.protocolhandlers.demo.entity.DemoEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author wei
 * @Time 2020/3/21
 * @Description 业务处理
 */
@Slf4j
public class DemoHandler extends SimpleChannelInboundHandler<DemoEntity> {

    /**
     * 业务处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DemoEntity msg) throws Exception {
        log.info("received demo entity: [{}]", msg.toString());
    }

    /**
     * 断开连接时将其剔除
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DeviceManagement.removeDeviceByChannel(ctx.channel());
    }

    /**
     * exception handle
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("device code: [{}] 发生exception: [{}]",
                DeviceManagement.getDeviceCodeByChannel(ctx.channel()),
                cause.getMessage());
    }
}
