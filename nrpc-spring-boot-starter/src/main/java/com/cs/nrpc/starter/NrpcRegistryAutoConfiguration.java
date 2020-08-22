package com.cs.nrpc.starter;

import com.cs.common.zk.ServiceRegistry;
import com.cs.nrpcserver.annotaion.EnableNRpcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EnableNRpcConfiguration.class)
@EnableConfigurationProperties(NrpcProperties.class)
@ConditionalOnProperty(prefix = "spring.nrpc", name = "server", havingValue = "true")
public class NrpcRegistryAutoConfiguration {

    @Autowired
    private NrpcProperties nrpcProperties;

    @Bean
    // 当 BeanFactory 中没有时注入
    @ConditionalOnMissingBean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistry(nrpcProperties.getRegistry());
    }
}
