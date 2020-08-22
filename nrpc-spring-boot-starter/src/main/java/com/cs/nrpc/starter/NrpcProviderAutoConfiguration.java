package com.cs.nrpc.starter;

import com.cs.common.zk.ServerInfo;
import com.cs.common.zk.ServiceRegistry;
import com.cs.nrpcserver.annotaion.EnableNRpcConfiguration;
import com.cs.nrpcserver.netty.server.NRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NrpcProperties.class)
@ConditionalOnClass(EnableNRpcConfiguration.class)
// todo zk?
@AutoConfigureAfter(NrpcRegistryAutoConfiguration.class)
// todo ?
@ConditionalOnProperty(prefix = "spring.nrpc", name = "server", havingValue = "true")
public class NrpcProviderAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(NrpcProviderAutoConfiguration.class);

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private NrpcProperties nrpcProperties;

    @Bean
    @ConditionalOnMissingBean
    public NRpcServer nrpcServer() {
        ServerInfo serverInfo = new ServerInfo(nrpcProperties.getHost(), nrpcProperties.getPort());

        NRpcServer nRpcServer = new NRpcServer(serverInfo, serviceRegistry);
        Thread thread = new Thread(() -> {
            try {
                nRpcServer.start();
            } catch (InterruptedException e) {
                log.error("nrpc-server-starter start error [{}]", e.getMessage());
                // 用来结束当前正在运行中的java虚拟机。System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序。
                System.exit(0);
            }
        });
        thread.start();
        return nRpcServer;
    }
}
