package com.cs.definedserver.protocolhandlers.demo;


import com.cs.definedserver.constants.ProtocolConstant;
import com.cs.definedserver.protocolhandlers.demo.decoder.DemoNewEntityDecoder;
import com.cs.definedserver.protocolhandlers.demo.decoder.DemoOldEntityDecoder;
import com.cs.definedserver.protocolhandlers.demo.decoder.DemoStartDecoder;
import com.cs.definedserver.protocolhandlers.demo.handler.DemoHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.stereotype.Component;

/**
 * @Author wei
 * @Time 2020/3/16
 * @Description
 */
@Component(ProtocolConstant.DEMO)
public class DemoInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 基于长度域拆包器 协议中指定了长度
                .addLast(new DemoStartDecoder())
                .addLast(new DemoOldEntityDecoder())
                .addLast(new DemoNewEntityDecoder())
                .addLast(new DemoHandler());
    }

}
