package com.example.ms.venta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//@EnableJpaRepositories

@SpringBootApplication

@EnableFeignClients
public class MsVentaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsVentaApplication.class, args);
    }

}
