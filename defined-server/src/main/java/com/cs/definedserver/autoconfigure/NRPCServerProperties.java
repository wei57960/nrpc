package com.cs.definedserver.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(
        prefix = "nrpc"
)
@Data
public class NRPCServerProperties {

    private Map<Integer, String> protocols;

}
