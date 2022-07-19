package com.tw.login.user.service;

import com.tw.login.user.entity.User;
import com.tw.login.user.exception.PasswordIncorrectException;
import com.tw.login.user.exception.UserAlreadyPresentException;
import com.tw.login.user.exception.UserNotRegisteredException;
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

    public void add(User user) throws UserAlreadyPresentException {
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isPresent()) {
            throw new UserAlreadyPresentException("User already present");
        }
        userRepository.save(user);
    }

    public void validate(User user) throws UserNotRegisteredException, PasswordIncorrectException {
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("User Not Registered");
        }
        if (!(user.getPassword().equals(userOptional.get().getPassword()))) {
            throw new PasswordIncorrectException("Incorrect password");
        }
    }

    public User getDetails(String userName) throws UserNotRegisteredException {
        return userRepository.findByUserName(userName).orElseThrow(() -> new UserNotRegisteredException("User Not Registered"));
    }
}

