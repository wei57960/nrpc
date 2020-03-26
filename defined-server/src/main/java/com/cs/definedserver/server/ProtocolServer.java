package com.cs.definedserver.server;

import com.cs.definedserver.constants.DefaultConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

/**
 * @Author wei
 * @Time 2020/3/14
 * @Description 根据协议启动的 Netty 端口 根据协议配置不同的handler
 */
public class ProtocolServer {

    private Integer port;
    private ChannelInitializer channelInitializer;
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();
    private static final Logger log = LoggerFactory.getLogger(ProtocolServer.class);


    public ProtocolServer(int port, ChannelInitializer channelInitializer) {
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    public ProtocolServer(ChannelInitializer channelInitializer) {
        this.port = DefaultConstant.DEFAULT_INCREASING_PORT_START;
        this.channelInitializer = channelInitializer;
    }

    public Integer getPort() {
        return this.port;
    }

    public void start() {
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(this.channelInitializer);
        bind(port);
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        log.info("成功关闭 port: [{}] 的 server", port);
    }

    /**
     * 绑定某个端口
     *
     * @param port
     */
    private void bind(final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("port:[{}] started. 使用的ChannelInitializer为 [{}]"
                        , this.port, this.channelInitializer.getClass().getSimpleName());
            } else {
                log.error("port:[{}] 绑定失败 !", port);
            }
        });
    }

    /**
     * 绑定不上则端口递增进行绑定
     *
     * @return 绑定上的端口
     */
    private Integer increasingBind(int port) {
        serverBootstrap.bind().addListener(future -> {
            if (future.isSuccess()) {
                log.info("绑定端口 [{}] 成功", port);
            } else {
                log.error("绑定端口 [{}] 失败 尝试递增绑定", port);
                increasingBind(port + 1);
            }
        });
        return port;
    }


}
