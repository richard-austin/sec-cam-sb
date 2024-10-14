package com.server

import com.server.configuration.ContextRefreshedListener
import com.server.services.UserService
import com.server.web.dto.UserDto
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.env.Environment

@SpringBootApplication(/*exclude = SecurityAutoConfiguration.class*/)
class SecCamSbApplication {
    static void main(String[] args) {
        SpringApplication app = new SpringApplication(SecCamSbApplication.class)
        app.addListeners(new ContextRefreshedListener())
        app.run(args)
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
    CommandLineRunner run(UserService userService) {
        return (String[] args) -> {
            if(!userService.roleExists('ROLE_CLIENT'))
                userService.addRole('ROLE_CLIENT')

            if(!userService.userNameExists('austin'))
                userService.registerNewUserAccount(new UserDto(username: "austin", password:"password", email: "a@b.com", cloudAccount: false, firstName: "Richard", lastName: "Austin"))
            if(!userService.userNameExists('cloud'))
                userService.registerNewUserAccount(new UserDto(username: "cloud", password:"password", email: "a@c.com", cloudAccount: true, header: "123123123", firstName: "Cloud", lastName: "Server"))
        }
    }

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return (args) -> {
            System.out.println("message from application.properties " + environment.getProperty("spring.jpa.properties.hibernate.globally_quoted_identifiers"))
        }
    }
}
