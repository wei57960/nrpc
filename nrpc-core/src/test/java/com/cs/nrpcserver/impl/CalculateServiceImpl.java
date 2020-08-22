package com.cs.nrpcserver.impl;

import com.cs.nrpcserver.inf.CalculateService;
import com.cs.nrpcserver.annotaion.NRpcProvider;

@NRpcProvider(interfaceClass = CalculateService.class)
public class CalculateServiceImpl implements CalculateService {

    @Override
    public String add(int a, int b) {
        return String.valueOf(a + b);
    }

}
