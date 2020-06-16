package com.cs.nrpcserver.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author wei
 * @Time 2020/3/28
 * @Description NRpc 启动
 */
@Component
public class NRpcBootstrap {

    @Value("${nrpc.server.host}")
    private String host;

    @Value("${nrpc.server.port}")
    private Integer port;

    @Value("${nrpc.zk.serverhost}")
    private String zkServerHost;

//    @PostConstruct
//    public void bootstrapStart() {
//        ServiceRegistry serviceRegistry = new ServiceRegistry(zkServerHost);
//        ServerInfo serverInfo = new ServerInfo();
//        serverInfo.setHost(host);
//        serverInfo.setPort(port);
//        serviceRegistry.register(CalculateService.class.getName(), serverInfo);
//        NettyServer nettyServer = new NettyServer(serverInfo, serviceRegistry);
//        nettyServer.start();
//
//    }

}
