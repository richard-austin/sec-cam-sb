package com.server.services

import com.server.error.UserAlreadyExistException
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
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + accountDto.getEmail())
        }
        final User user = new User()

        user.setFirstName(accountDto.getFirstName())
        user.setLastName(accountDto.getLastName())
        user.setUsername(accountDto.getUsername())
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()))
        user.setEmail(accountDto.getEmail())
        user.setUsing2FA(accountDto.isUsing2FA())
        user.setEnabled(true)
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")))
        return userRepository.save(user)
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null
    }
}
