package com.cs.consumer.controller;

import com.cs.api.HelloNrpcService;
import com.cs.nrpcserver.annotaion.NRpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerHelloController {

    @NRpcReference(interfaceClass = HelloNrpcService.class, version = "1.0.0")
    private HelloNrpcService helloNrpcService;

    @GetMapping("/nrpc/{words}")
    public String hello(@PathVariable String words) {
        return helloNrpcService.hello(words);
    }

}
