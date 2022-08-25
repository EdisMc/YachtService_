package com.example.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example"})
@ComponentScan(basePackages = {"com.example"})
public class YachtServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YachtServiceApplication.class, args);
    }

}
