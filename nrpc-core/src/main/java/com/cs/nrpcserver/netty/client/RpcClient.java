package com.cs.nrpcserver.netty.client;

import com.cs.common.model.NRpcRequest;
import com.cs.common.model.NRpcResponse;
import com.cs.nrpcserver.netty.client.initializer.ClientDecoder;
import com.cs.nrpcserver.netty.client.initializer.ClientEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author wei
 * @Time 2020/4/11
 * @Description bio 请求
 */
@Slf4j
public class RpcClient extends SimpleChannelInboundHandler<NRpcResponse> {

    private String host;
    private int port;
    private NRpcRequest request;
    private NRpcResponse response;
    private EventLoopGroup worker = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();


    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * bio 阻塞 向server 端发送请求
     * todo 建立一个连接池 长连接 ?
     *
     * @param request NrpcRequset
     * @return
     * @throws InterruptedException
     */
    public NRpcResponse request(NRpcRequest request) throws InterruptedException {
        this.request = request;
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ClientEncoder())
                                .addLast(new ClientDecoder())
                                .addLast(RpcClient.this);
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            return response;
        } finally {
            worker.shutdownGracefully();
        }

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NRpcResponse response) throws Exception {
        this.response = response;
        log.info("客户端接收数据: [{}]", response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("rpc 失败");
        this.response = NRpcResponse.builder()
                .requestId(request.getRequestId())
                .result(null)
                .errorCode(-1)
                .errorMessage(cause.getMessage())
                .requestTime(System.currentTimeMillis() - request.getStartTime())
                .build();
        ctx.close();
    }

}
