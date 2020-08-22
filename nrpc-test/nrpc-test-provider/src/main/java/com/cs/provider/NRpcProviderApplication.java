package com.cs.provider;

import com.cs.nrpcserver.annotaion.EnableNRpcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNRpcConfiguration
public class NRpcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NRpcProviderApplication.class, args);
    }

}
