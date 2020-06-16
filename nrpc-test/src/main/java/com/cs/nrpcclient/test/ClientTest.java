package com.cs.nrpcclient.test;

import com.cs.common.inf.CalculateService;
import com.cs.common.proxy.DefaultProxy;
import com.cs.common.zk.ServiceDiscover;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author wei
 * @Time 2020/4/11
 * @Description 采用直接调用的方式测试
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) throws InterruptedException {

        String zkAddress = "127.0.0.1:2181";
        ServiceDiscover serviceDiscover = new ServiceDiscover(zkAddress);

        String serverHost = serviceDiscover.discoverClass(CalculateService.class);
        DefaultProxy defaultProxy = new DefaultProxy(serverHost);
        CalculateService calculateService = defaultProxy.createProxy(CalculateService.class);
        String res = calculateService.add(1, 1);
        System.out.println("从服务端返回的数据: " + res);

    }

}
