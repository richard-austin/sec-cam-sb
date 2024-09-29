package com.server

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import server.BinarySearcher

@SpringBootApplication
class SecCamSbApplication {

    static void main(String[] args) {
        SpringApplication.run(SecCamSbApplication, args)

        def searcher = new BinarySearcher()

        byte[] bytes = [1,2,3,4,5,6,7,8,99,124]
        byte[] ninenine = [99];

        def idx = searcher.indexOf(bytes, ninenine)
        System.out.println("Index = ${idx}")
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return (args) -> {

//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//
//        };
        }
    }
}
