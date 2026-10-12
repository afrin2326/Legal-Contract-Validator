package com.legalvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LegalValidatorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LegalValidatorApplication.class, args);
        System.out.println("========================================");
        System.out.println("🚀 Smart Contract Validator Started!");
        System.out.println("📍 Backend: http://localhost:8985");
        System.out.println("📍 Frontend: http://localhost:3000");
        System.out.println("========================================");
    }
}