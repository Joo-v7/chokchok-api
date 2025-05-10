package com.chokchok.chokchokapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ChokchokApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChokchokApiApplication.class, args);
    }

}
