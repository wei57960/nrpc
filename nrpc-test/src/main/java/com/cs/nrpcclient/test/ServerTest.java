package com.cs.nrpcclient.test;

import com.cs.common.zk.ServerInfo;
import com.cs.common.zk.ServiceRegistry;
import com.cs.nrpcserver.netty.NettyServer;

/**
 * @Author wei
 * @Time 2020/4/12
 * @Description server 端 test
 */
public class ServerTest {

    public static void main(String[] args) throws InterruptedException {
        String zkServer = "127.0.0.1:2181";
        ServiceRegistry serviceRegistry = new ServiceRegistry(zkServer);

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setHost("127.0.0.1");
        serverInfo.setPort(9876);

        NettyServer nettyServer = new NettyServer(serverInfo, serviceRegistry);
        nettyServer.start();

    }

}
