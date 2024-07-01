package com.example.pfabackendfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.example.pfabackendfinal.repository"})
//@ComponentScan(basePackages = {"com.example.pfabackendfinal.services","com.example.pfabackendfinal.repository","com.example.pfabackendfinal.config"})
//@EntityScan(basePackages = {"com/example/pfabackendfinal/entity"})
public class PfaBackendFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfaBackendFinalApplication.class, args);
    }

}
