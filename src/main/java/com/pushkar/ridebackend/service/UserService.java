package com.pushkar.ridebackend.service;

import com.pushkar.ridebackend.model.User;
import com.pushkar.ridebackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword , String role) {

        if(userRepository.findByUsername(username).isPresent()) throw new RuntimeException("Username already exists");

        String hashedPassword = passwordEncoder.encode(rawPassword);

        return userRepository.save(new User(username, hashedPassword, role));
    }
}
