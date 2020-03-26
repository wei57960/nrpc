package com.cs.definedserver.exceptions;

/**
 * @Author wei
 * @Time 2020/3/14
 * @Description
 */
public class DefinedServerException extends RuntimeException {

    private String code;

    public DefinedServerException(DefinedServerExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public String getCode() {
        return code;
    }
}
