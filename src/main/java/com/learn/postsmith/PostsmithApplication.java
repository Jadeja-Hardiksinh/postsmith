package com.learn.postsmith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PostsmithApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostsmithApplication.class, args);
    }

}
