package com.cs.nrpc.starter;

import com.cs.common.constant.DefaultConfigConstant;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ToString
@ConfigurationProperties(prefix = "spring.nrpc")
public class NrpcProperties {

    enum ProxyType {
        JDK, CGLIB
    }

    /**
     * rpc name
     */
    private String name;
    // todo 修改为默认和springboot扫描的包一样
    private String basePackage;
    private Integer weight=DefaultConfigConstant.DEFAULT_WEIGHT;
    private String host = DefaultConfigConstant.DEFAULT_HOST;
    private Integer port = DefaultConfigConstant.DEFAULT_PORT;

    private ProxyType proxyType = ProxyType.JDK;


    private String registry = DefaultConfigConstant.REGISTRY_ADDRESS;
    private String discover = DefaultConfigConstant.DISCOVER_ADDRESS;
}
