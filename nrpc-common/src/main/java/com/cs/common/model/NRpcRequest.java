package com.cs.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author wei
 * @Time 2020/6/10
 * @Description nrpc request model
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NRpcRequest {

    private String requestId;

    /**
     * service interface
     */
    private String serviceName;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;

    private String version;

    private Long startTime;

}
