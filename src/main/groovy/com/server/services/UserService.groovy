package com.server.services

import com.server.error.UserAlreadyExistException
import com.server.model.Role
import com.server.model.User
import com.server.persistance.dao.UserRepository
import com.server.persistance.dao.RoleRepository
import com.server.web.dto.UserDto
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class UserService {
    @Autowired
    UserRepository userRepository
    @Autowired
    RoleRepository roleRepository

    @Autowired
    private PasswordEncoder passwordEncoder

    User registerNewUserAccount(final UserDto accountDto) {
        if (userNameExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that username: " + accountDto.getUsername())
        }
        final User user = new User()

        user.setFirstName(accountDto.getFirstName())
        user.setLastName(accountDto.getLastName())
        user.setUsername(accountDto.getUsername())
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()))
        user.setEmail(accountDto.getEmail())
        user.setUsing2FA(accountDto.isUsing2FA())
        user.setEnabled(true)
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_CLIENT")))
        return userRepository.save(user)
    }

    Role addRole(final String roleName) {
        def role = new Role(roleName)
        return roleRepository.save(role)
    }

    boolean roleExists(String roleName) {
        return roleRepository.findByName(roleName) != null
    }

    boolean userNameExists(final String username) {
        return userRepository.findByUsername(username) != null
    }
}
