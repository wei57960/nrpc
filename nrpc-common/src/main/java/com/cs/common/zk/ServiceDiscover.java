package com.cs.common.zk;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @Author wei
 * @Time 2020/3/31
 * @Description zk 服务发现
 */
@Slf4j
public class ServiceDiscover {

    private static final int ZK_SESSION_TIMEOUT = 5000;
    private static final int ZK_CONNECTION_TIMEOUT = 1000;
    private String zkServerAddress;

    public ServiceDiscover(String zkServerAddress) {
        this.zkServerAddress = zkServerAddress;
    }

    public String discoverClass(Class clazz) {
        String className = clazz.getName();
        return discover(className);
    }

    /**
     * 发现 service 地址
     *
     * @param serviceName
     * @return
     */
    public String discover(String serviceName) {
        ZkClient zkClient = new ZkClient(zkServerAddress, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
        String servicePath = RegistryUtil.getRemoteService(serviceName);
        List<String> serverNames = zkClient.getChildren(servicePath);
        serverNames.stream().forEach(e -> log.info("find server names from zookeeper [{}]", e));
        // todo 服务发现的负载均衡
        if (serverNames.size() == 0) {
            throw new RuntimeException("not found services");
        }
        String serverName = serverNames.get(0);
        String serverAddress = zkClient.readData(servicePath + "/" + serverName);
        return serverAddress;
    }

}
