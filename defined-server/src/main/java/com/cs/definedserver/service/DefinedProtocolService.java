package com.cs.definedserver.service;

import com.cs.definedserver.exceptions.DefinedServerException;
import com.cs.definedserver.exceptions.DefinedServerExceptionEnum;
import com.cs.definedserver.protocolhandlers.DefinedProtocolManagement;
import com.cs.definedserver.server.ProtocolServer;
import io.netty.channel.ChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wei
 * @Time 2020/3/21
 * @Description
 */
@Service
public class DefinedProtocolService {

    // todo rpc 暴露服务

    @Autowired
    private DefinedProtocolManagement definedProtocolManagement;

    /**
     * 指定端口 协议启动服务
     *
     * @param port     端口
     * @param protocol 指定协议
     */
    public void startDefinedProtocolService(int port, String protocol) {
        ChannelInitializer channelInitializer = definedProtocolManagement.getChannelHandlerByType(protocol);
        if (channelInitializer != null) {
            ProtocolServer protocolServer = new ProtocolServer(port, channelInitializer);
            protocolServer.start();
        }
    }

    /**
     * 根据协议开启服务
     *
     * @param protocol 指定协议
     * @return 启动的端口
     */
    public Integer startDefinedProtocolService(String protocol) {
        ChannelInitializer channelInitializer = definedProtocolManagement.getChannelHandlerByType(protocol);
        if (channelInitializer != null) {
            ProtocolServer protocolServer = new ProtocolServer(channelInitializer);
            protocolServer.start();
            return protocolServer.getPort();
        }
        throw new DefinedServerException(DefinedServerExceptionEnum.START_FAIL);
    }

}
