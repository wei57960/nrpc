package com.cs.common.zk;

/**
 * @Author wei
 * @Time 2020/3/28
 * 目录结构：
 * |--- /nrpc
 * |-----------/services
 * |--------------------/helloWordService
 * |------------------------------------/server-1
 * |------------------------------------/server-2
 * |-----------/config
 * |-------------------/nrpc-server
 * |-----------/locks
 * |-------------------/rpc-lock
 */
public class RegistryUtil {

    private static final String ROOT_NAME = "/nrpc";

    private static final String SERVICES_ROOT_NAME = "services";

    private static final String SERVICE_PREFIX = "server-";

    public static String getRemoteService(String serviceName) {
        return getServiceParentPath() + "/" + serviceName;
    }

    public static String getServiceParentPath() {
        return ROOT_NAME + "/" + SERVICES_ROOT_NAME;
    }

    public static String getServerPath(String serviceName) {
        return getRemoteService(serviceName) + "/" + SERVICE_PREFIX;
    }

    public static String getServerNodeData(ServerInfo serverInfo) {
        return serverInfo.getHost() + ":" + serverInfo.getPort();
    }
}
