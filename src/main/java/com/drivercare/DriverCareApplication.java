package com.drivercare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DriverCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverCareApplication.class, args);
    }

}
