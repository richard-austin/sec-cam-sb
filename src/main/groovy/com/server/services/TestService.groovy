package com.server.services

import org.springframework.stereotype.Service

@Service
class TestService {
    int x = 2
    String getInfoFromService() {
        return "This is the info from the service ${x}"
    }
}
