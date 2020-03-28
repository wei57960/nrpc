package com.cs.definedserver.exceptions;

public enum DefinedServerExceptionEnum {

    HANDLER_NOT_EXISTS("5000", "寻找的 handler 不存在"),
    DEVICE_NOT_CONNECT("50001", "该设备没有在连接"),
    SEND_MESSAGE_FAIL("50002", "发送消息失败"),
    START_FAIL("50003", "启动端口失败"),
    ;

    private String code;

    private String message;

    DefinedServerExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
