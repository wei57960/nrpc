package com.cs.nrpcserver.netty;

import com.cs.common.inf.CalculateService;
import com.cs.common.util.ServiceNameUtil;
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


@Slf4j
public class NettyServer {

    private ServerInfo serverInfo;
    private Integer port;
    private ServiceRegistry serviceRegistry;
    private NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    // todo 扫描范围 放到配置里？
    String scanPackage = "com.cs.nrpcserver";
    public static Map<String, Object> providerServiceBeans = new ConcurrentHashMap<>();

    public NettyServer(ServerInfo serverInfo, ServiceRegistry serviceRegistry) {
        this.serverInfo = serverInfo;
        this.serviceRegistry = serviceRegistry;
        this.port = serverInfo.getPort();
    }

    public void start() throws InterruptedException {
        registerProviderServiceToMap(scanPackage);
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RpcChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 1024) // todo client set serializer size 1024
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
            String serviceName = ServiceNameUtil.getServiceName(interfaceClass, version);
            Object providerBean;
            try {
                providerBean = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            providerServiceBeans.put(serviceName, providerBean);
        }
    }

    // todo zookeeper 的注册安全？
    //todo 注册所有被修饰的service

    /**
     * 将提供的服务 以及服务对应请求地址注册到 zk 上
     */
    private void registerToZookeeper() {
        serviceRegistry.register(CalculateService.class.getName(), serverInfo);
    }

}
