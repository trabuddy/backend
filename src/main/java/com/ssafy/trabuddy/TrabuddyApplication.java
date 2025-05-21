package com.ssafy.trabuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrabuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrabuddyApplication.class, args);
    }

}
