package com.cs.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author wei
 * @Time 2020/6/10
 * @Description nrpc response model
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NRpcResponse {

    private String requestId;

    private Object result;

    private int errorCode;

    private long requestTime;

    private String errorMessage;

}
