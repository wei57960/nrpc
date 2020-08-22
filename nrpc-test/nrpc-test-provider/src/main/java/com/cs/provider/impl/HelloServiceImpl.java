package com.cs.provider.impl;

import com.cs.api.HelloNrpcService;
import com.cs.nrpcserver.annotaion.NRpcProvider;

@NRpcProvider(interfaceClass = HelloNrpcService.class, version = "1.0.0")
public class HelloServiceImpl implements HelloNrpcService {

    @Override
    public String hello(String word) {
        return "nrpc says " + word;
    }

}
