package com.cs.nrpcserver.netty.server;


import com.cs.common.util.NRpcServiceUtil;
import com.cs.common.zk.ServerInfo;
import com.cs.common.zk.ServiceRegistry;
import com.cs.nrpcserver.annotaion.NRpcProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc server 端启动
 */
@Slf4j
public class NRpcServer {

    private ServerInfo serverInfo;
    private Integer port;
    private ServiceRegistry serviceRegistry;
    private NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    public static Map<String, Object> providerServiceBeans = new ConcurrentHashMap<>();

    public NRpcServer(ServerInfo serverInfo, ServiceRegistry serviceRegistry) {
        this.serverInfo = serverInfo;
        this.serviceRegistry = serviceRegistry;
        this.port = serverInfo.getPort();
    }

    public void start(String scanPackage) throws InterruptedException {
        registerProviderServiceToMap(scanPackage);
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RpcChannelInitializer())
                .childOption(ChannelOption.SO_KEEPALIVE, true);


        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        registerToZookeeper();
        channelFuture.channel().closeFuture().sync();
        closeServer();
    }

    private void closeServer() {
        serviceRegistry.shutdown();
        bossEventLoopGroup.shutdownGracefully();
        workerEventLoopGroup.shutdownGracefully();
    }


    private void bind(ServerBootstrap serverBootstrap) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    log.info("start port [{}] success", port);
                } else {
                    log.error("start port [{}] success", port);
                }
            }
        });
    }

    /**
     * register provider services to local Map
     *
     * @param scanPackage
     */
    private void registerProviderServiceToMap(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> providerClass = reflections.getTypesAnnotatedWith(NRpcProvider.class);
        for (Class<?> clazz : providerClass) {
            NRpcProvider provider = clazz.getAnnotation(NRpcProvider.class);
            Class<?> interfaceClass = provider.interfaceClass();
            String version = provider.version();
            String serviceName = NRpcServiceUtil.getServiceName(interfaceClass, version);
            Object providerBean;
            try {
                providerBean = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            providerServiceBeans.put(serviceName, providerBean);
        }
    }


    /**
     * 将提供的服务 以及服务对应请求地址注册到 zk 上
     */
    private void registerToZookeeper() {
        for (String serviceName : providerServiceBeans.keySet()) {
            serviceRegistry.register(serviceName, serverInfo);
        }
    }

}
