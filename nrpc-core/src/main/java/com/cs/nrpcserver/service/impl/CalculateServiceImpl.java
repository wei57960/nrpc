package com.cs.nrpcserver.service.impl;

import com.cs.common.inf.CalculateService;
import com.cs.nrpcserver.annotaion.NRpcProvider;

@NRpcProvider(interfaceClass = CalculateService.class)
public class CalculateServiceImpl implements CalculateService {

    @Override
    public String add(int a, int b) {
        return String.valueOf(a + b);
    }

}
