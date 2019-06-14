package com.qfant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class HtgyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HtgyApplication.class, args);
    }

}
