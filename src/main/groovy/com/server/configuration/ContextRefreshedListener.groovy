package com.server.configuration

import com.server.services.UserService
import com.server.web.dto.UserDto
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

class ContextRefreshedListener  implements ApplicationListener<ContextRefreshedEvent> {
    UserService userService

    @Override
    void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("ContextRefreshedEvent")
    }
}
