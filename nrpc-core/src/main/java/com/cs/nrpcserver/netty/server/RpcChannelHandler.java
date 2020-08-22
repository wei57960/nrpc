package com.cs.nrpcserver.netty.server;

import com.cs.common.model.NRpcRequest;
import com.cs.common.model.NRpcResponse;
import com.cs.common.util.ServiceNameUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class RpcChannelHandler extends SimpleChannelInboundHandler<NRpcRequest> {


    // todo 将请求服务存放

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NRpcRequest request) throws Exception {
        log.info("nrpc server received message : [{}]", request);
        Object providerServiceBean = NRpcServer.providerServiceBeans.get(
                ServiceNameUtil.getServiceName(
                        Class.forName(request.getServiceName()), request.getVersion()
                ));
        if (Objects.isNull(providerServiceBean)) {
            NRpcResponse response = NRpcResponse.builder()
                    .result(request.getRequestId())
                    .requestTime(System.currentTimeMillis() - request.getStartTime())
                    .errorMessage("can't found service < " + request.getServiceName() + " >")
                    .build();
            ctx.pipeline().writeAndFlush(response)
                    // todo what is this
                    .addListener(ChannelFutureListener.CLOSE);
        }
        Method method = providerServiceBean.getClass().getMethod(request.getMethodName(), request.getParamTypes());
        method.setAccessible(true);
        try {
            Object result = method.invoke(providerServiceBean, request.getParams());
            NRpcResponse nRpcResponse = NRpcResponse.builder().
                    requestId(request.getRequestId())
                    .result(result)
                    .requestTime(System.currentTimeMillis() - request.getStartTime())
                    .errorCode(0)
                    .build();
            ctx.pipeline()
                    .writeAndFlush(nRpcResponse)
                    .addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            NRpcResponse nRpcResponse = NRpcResponse.builder()
                    .requestId(request.getRequestId())
                    .requestTime(System.currentTimeMillis() - request.getStartTime())
                    .errorMessage("-1")
                    .errorMessage("rpc 调用发生错误  " + e.getMessage())
                    .build();
            ctx.pipeline()
                    .writeAndFlush(nRpcResponse)
                    .addListener(ChannelFutureListener.CLOSE);
        }

    }


}
