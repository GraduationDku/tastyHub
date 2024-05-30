package com.example.tastyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@EntityScan(basePackages = {"com.example.tastyhub.common.domain.chat.entity"})
public class TastyhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TastyhubApplication.class, args);
    }

}
