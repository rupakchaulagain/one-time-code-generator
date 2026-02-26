package com.foneloan.ecompass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcomPassApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcomPassApplication.class, args);
    }
}
