package com.example.msalmacen;

import com.example.msalmacen.repository.MateriaPrimaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
public class MsAlmacenApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAlmacenApplication.class, args);
    }

}