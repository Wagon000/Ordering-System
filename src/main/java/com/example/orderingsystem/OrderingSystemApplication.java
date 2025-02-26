package com.example.orderingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Restaurant Ordering System Application
 * 
 * @author Zeyu Wang
 * @version 2.0
 * @date 2024-02-26
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.example.orderingsystem.mapper")
@EnableAsync // Enable async
@EnableScheduling // Enable scheduling
@EnableTransactionManagement // Enable transaction management
@ServletComponentScan // Scan servlet components
public class OrderingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderingSystemApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("=                                                        =");
        System.out.println("=                  点餐系统启动成功                        =");
        System.out.println("=                                                        =");
        System.out.println("==========================================================");
    }
} 