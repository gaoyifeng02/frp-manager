package com.gaoyifeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * FPC应用程序主类
 */
@SpringBootApplication
@EnableScheduling
public class FpcApplication {
    public static void main(String[] args) {
        SpringApplication.run(FpcApplication.class, args);
    }
}
