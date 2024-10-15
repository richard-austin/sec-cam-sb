package com.server

import com.server.configuration.ContextRefreshedListener
import com.server.model.Role
import com.server.persistance.dao.RoleRepository
import com.server.securingweb.MvcConfig
import com.server.services.UserService
import com.server.web.dto.UserDto
import jakarta.validation.ConstraintViolation
import jakarta.validation.ValidatorFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.env.Environment

import jakarta.xml.*
import jakarta.validation.*;

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

    @Autowired
    RoleRepository roleRepository

    @Bean
    CommandLineRunner run(UserService userService) {
        return (String[] args) -> {
            if(!userService.roleExists('ROLE_CLIENT'))
                userService.addRole('ROLE_CLIENT')

            if(!userService.roleExists('ROLE_CLOUD'))
                userService.addRole('ROLE_CLOUD')

            if(!userService.userNameExists('austin')) {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();

                Role role = roleRepository.findByName("ROLE_CLIENT")
                 if(role != null) {
                     var user = new UserDto(username: "austin", password: "password", matchingPassword: "password", email: "a@b.com", cloudAccount: false, role: role.getId())
                     Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
                     userService.registerNewUserAccount(user)
                 }
            }
            if(!userService.userNameExists('cloud')) {
                Role role = roleRepository.findByName("ROLE_CLIENT")
                if (role != null)
                    userService.registerNewUserAccount(new UserDto(username: "cloud", password: "password", matchingPassword: "password", email: "a@c.com", cloudAccount: true, header: "123123123", role: role.getId()))
            }
        }
    }

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return (args) -> {
            System.out.println("message from application.properties " + environment.getProperty("spring.jpa.properties.hibernate.globally_quoted_identifiers"))
        }
    }
}
