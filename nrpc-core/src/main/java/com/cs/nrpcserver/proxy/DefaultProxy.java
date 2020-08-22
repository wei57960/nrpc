package com.cs.nrpcserver.proxy;

import com.cs.common.model.NRpcRequest;
import com.cs.common.model.NRpcResponse;
import com.cs.nrpcserver.netty.client.RpcClient;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author wei
 * @Time 2020/4/11
 * @Description 默认使用 jdk 代理
 */
public class DefaultProxy {

    private String serverHost;

    public DefaultProxy(String serverHost) {
        this.serverHost = serverHost;
    }

    public <T> T createProxy(Class<?> interfaceClass) {
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (Object proxy, Method method, Object[] args) -> {
                    Object result = senRequest(proxy, method, args, interfaceClass);
                    if (result != null) {
                        return result;
                    }
                    throw new RuntimeException("request fail");
                });
        return (T) object;
    }

    private Object senRequest(Object proxy, Method method, Object[] args, Class<?> interfaceClass) throws InterruptedException {
        if (!StringUtils.isEmpty(serverHost)) {
            String host = serverHost.split(":")[0];
            int port = Integer.parseInt(serverHost.split(":")[1]);

            RpcClient rpcClient = new RpcClient(host, port);
            // todo how Dubbo requestId generate ?
            // todo why use timestamp
            NRpcRequest request = NRpcRequest.builder()
                    .requestId(UUID.randomUUID().toString())
                    .serviceName(interfaceClass.getName())
                    .methodName(method.getName())
                    .paramTypes(method.getParameterTypes())
                    .params(args)
                    .startTime(System.currentTimeMillis())
                    .build();
            NRpcResponse response = rpcClient.request(request);

            if (response == null) {
                throw new RuntimeException("error request");
            }
            if (response.getErrorCode() != 0) {
                throw new RuntimeException(response.getErrorMessage());
            }
            return response.getResult();
        }
        throw new RuntimeException("not found server " + serverHost);
    }

}
