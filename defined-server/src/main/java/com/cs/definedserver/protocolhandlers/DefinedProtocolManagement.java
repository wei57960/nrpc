package com.cs.definedserver.protocolhandlers;

import io.netty.channel.ChannelInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author wei
 * @Time 2020/3/14
 * @Description 管理所有的 协议和其 ChannelInitializer
 */
@Component
public class DefinedProtocolManagement {

    private static final Logger log = LoggerFactory.getLogger(DefinedProtocolManagement.class);

    /**
     * key ： protocol name
     */
    @Autowired
    private Map<String, ChannelInitializer> handlers = new ConcurrentHashMap<>();


    public ChannelInitializer getChannelHandlerByType(String type) {
        ChannelInitializer channelInitializer = handlers.get(type);
        if (channelInitializer != null) {
            return channelInitializer;
        }
        log.error("[{}]类型的 handler 未定义", type);
        return null;
    }

}
