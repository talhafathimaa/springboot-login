package com.tw.login.user.service;

import com.tw.login.user.entity.User;
import com.tw.login.user.exception.InvalidDetailsException;
import com.tw.login.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(User user) throws InvalidDetailsException {
        if (user.getUserName().isEmpty()) {
            throw new InvalidDetailsException("Invalid Username");
        }
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isPresent()) {
            throw new InvalidDetailsException("User already present");
        }
        if (user.getPassword().length() > 5) {
            userRepository.save(user);
        } else {
            throw new InvalidDetailsException("Password should have at-least 6 characters");
        }
    }

    public void validate(User user) throws InvalidDetailsException {
        if (user.getUserName().isBlank()) {
            throw new InvalidDetailsException("Invalid Username");
        }
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isEmpty()) {
            throw new InvalidDetailsException("User Not Registered");
        }
        if (!(user.getPassword().equals(userOptional.get().getPassword()))) {
            throw new InvalidDetailsException("Incorrect password");
        }
    }

    public User getDetails(String userName) throws InvalidDetailsException {
        return userRepository.findByUserName(userName).orElseThrow(() -> new InvalidDetailsException("Invalid username"));

    }
}

