package com.server

import com.server.commands.User
import com.server.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@SpringBootApplication(/*exclude = SecurityAutoConfiguration.class*/)
class SecCamSbApplication {
    static void main(String[] args) {
        SpringApplication.run(SecCamSbApplication, args)
    }

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext ctx) {
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

    @Bean
    public CommandLineRunner run(UserRepository userRepository) {
        return (String[] args) -> {
            User user1 = new User("Bob", "bob@domain.com");
            User user2 = new User("Jenny", "jenny@domain.com");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.findAll().forEach(System.out::println);
        };
    }

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return (args) -> {
            System.out.println("message from application.properties " + environment.getProperty("spring.jpa.properties.hibernate.globally_quoted_identifiers"));
        };
    }

}
