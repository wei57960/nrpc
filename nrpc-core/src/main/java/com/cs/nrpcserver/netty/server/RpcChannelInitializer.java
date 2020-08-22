package com.cs.nrpcserver.netty.server;


import com.cs.nrpcserver.netty.client.initializer.ClientDecoder;
import com.cs.nrpcserver.netty.client.initializer.ClientEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author wei
 * @Time 2020/3/28
 * @Description
 */
public class RpcChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private static final Logger log = LoggerFactory.getLogger(RpcChannelInitializer.class);


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new ClientDecoder())
                .addLast(new ClientEncoder())
                .addLast(new RpcChannelHandler());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("发生错误 [{}] ", cause.getMessage());
        ctx.channel().close();
        ctx.close();
    }
}
