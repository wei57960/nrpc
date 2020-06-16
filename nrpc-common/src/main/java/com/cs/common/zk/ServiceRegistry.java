package com.cs.common.zk;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

/**
 * @Author wei
 * @Time 2020/3/31
 * @Description zk service 注册
 */
@Slf4j
public class ServiceRegistry {

    private static final int ZK_SESSION_TIMEOUT = 5000;
    private static final int ZK_CONNECTION_TIMEOUT = 1000;

    private final ZkClient zkClient;

    /**
     * 获得 ZK 连接
     *
     * @param zkServer zookeeper server host
     */
    public ServiceRegistry(String zkServer) {
        this.zkClient = new ZkClient(zkServer, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
    }

    /**
     * 注册数据到 ZK 上
     *
     * @param serviceName
     * @param serverInfo
     */
    public void register(String serviceName, ServerInfo serverInfo) {
        String serviceParentPath = RegistryUtil.getServiceParentPath();
        if (!zkClient.exists(serviceParentPath)) {
            zkClient.createPersistent(serviceParentPath,true);
        }
        String servicePath = RegistryUtil.getRemoteService(serviceName);
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
        }

        String serverPath = RegistryUtil.getServerPath(serviceName);
        String serverNodeData = RegistryUtil.getServerNodeData(serverInfo);
        // 创建顺序的节点 并存入数据
        String truePath = zkClient.createEphemeralSequential(serverPath, serverNodeData);
        // data: 顺序节点上的数据
        String data = zkClient.readData(truePath);
    }

    public void shutdown() {
        zkClient.close();
    }

}
