package com.cs.definedserver.server;


import com.cs.definedserver.autoconfigure.NRPCServerProperties;
import com.cs.definedserver.protocolhandlers.DefinedProtocolManagement;
import io.netty.channel.ChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author wei
 * @Time 2020/3/21
 * @Description
 */
@Component
public class DefinedServer {

    @Resource
    private NRPCServerProperties properties;

    @Autowired
    private DefinedProtocolManagement definedProtocolManagement;


    @PostConstruct
    public void start() {
        Map<Integer, String> protocolMap = properties.getProtocols();
        for (Map.Entry<Integer, String> map : protocolMap.entrySet()) {
            int port = map.getKey();
            ChannelInitializer channelInitializer =
                    definedProtocolManagement.getChannelHandlerByType(map.getValue());
            if (channelInitializer != null) {
                ProtocolServer protocolServer = new ProtocolServer(port, channelInitializer);
                protocolServer.start();
            }
        }
    }


}
