package com.civicpulse;

import com.civicpulse.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CivicPulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CivicPulseApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(UserService userService) {
        return args -> userService.createDefaultAdminIfNotExists();
    }
}
