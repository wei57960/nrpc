package com.cs.definedserver.protocolhandlers;


import com.cs.definedserver.exceptions.DefinedServerException;
import com.cs.definedserver.exceptions.DefinedServerExceptionEnum;
import com.cs.definedserver.utils.ByteUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wei
 * @Time 2020/3/14
 * @Description 设备与 Channel 之间的映射存储
 */
public class DeviceManagement {

    /**
     * key : deviceCode
     * value: channel
     */
    private static Map<String, Channel> deviceMap = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(DeviceManagement.class);


    public static Channel getChannelByDeviceCode(String deviceCode) {
        Channel channel = deviceMap.get(deviceCode);
        if (channel != null) {
            return channel;
        }
        throw new DefinedServerException(DefinedServerExceptionEnum.DEVICE_NOT_CONNECT);
    }

    /**
     * 主动给 device 发送报文
     *
     * @param deviceCode 设备编号
     * @param message    报文
     */
    public static void sendMessageToDevice(String deviceCode, byte[] message) {
        Channel channel = getChannelByDeviceCode(deviceCode);
        ChannelFuture future = channel.writeAndFlush(message);
        if (future.isSuccess()) {
            log.info("write message:[{}] to deviceCode:[{}]", ByteUtil.getByteArrayString(message), deviceCode);
        }
        throw new DefinedServerException(DefinedServerExceptionEnum.SEND_MESSAGE_FAIL);
    }

    public static void putDeviceChannelMap(String deviceCode, Channel channel) {
        Channel existedChannel = deviceMap.get(deviceCode);
        if (existedChannel != null) {
            if (existedChannel == channel) {
                return;
            }
        }
        deviceMap.put(deviceCode, channel);
        log.info("deviceCode:[{}] register successful in channel map", deviceCode);
    }

    public static void removeDeviceByChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : deviceMap.entrySet()) {
            Channel value = entry.getValue();
            if (channel == value) {
                deviceMap.remove(entry.getKey());
                log.info("deviceCode:[{}] removed from channel map ", entry.getKey());
            }
        }
    }

    public static String getDeviceCodeByChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : deviceMap.entrySet()) {
            Channel value = entry.getValue();
            if (channel == value) {
                return entry.getKey();
            }
        }
        return null;
    }

}
