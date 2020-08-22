package com.cs.consumer;

import com.cs.nrpcserver.annotaion.EnableNRpcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNRpcConfiguration
public class NRpcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NRpcConsumerApplication.class, args);
    }
}
