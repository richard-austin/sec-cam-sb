package com.server.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.server.commands.TestUser;
import com.server.repositories.TestUserRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

//    private final TestUserRepository userRepository;

    @Autowired
    public UserController(TestUserRepository userRepository) {
//        this.userRepository = userRepository;
    }

//    @GetMapping("/users")
//    public List<TestUser> getUsers() {
//        return (List<TestUser>) userRepository.findAll();
//    }

    @PostMapping("/users")
    @Secured("ROLE_USER")
    ResponseEntity<String> addUser(@Valid @RequestBody TestUser user) {
        return ResponseEntity.ok("User is valid");
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
