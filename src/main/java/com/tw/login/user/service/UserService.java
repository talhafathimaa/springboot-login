package com.tw.login.user.service;

import com.tw.login.user.entity.User;
import com.tw.login.user.exception.PasswordIncorrectException;
import com.tw.login.user.exception.UserAlreadyPresentException;
import com.tw.login.user.exception.UserNotRegisteredException;
import com.tw.login.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public void add(User user) throws UserAlreadyPresentException {
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isPresent()) {
            throw new UserAlreadyPresentException("User already present");
        }
        String encodedPassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public User validate(User user) throws UserNotRegisteredException, PasswordIncorrectException {
        Optional<User> userOptional = userRepository.findByUserName(user.getUserName());
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("User Not Registered");
        }
        if (!passwordEncoder.matches(user.getPassword(),userOptional.get().getPassword())) {
            throw new PasswordIncorrectException("Incorrect password");
        }
        return userOptional.get();
    }

    public User getDetails(Long id) throws UserNotRegisteredException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotRegisteredException("User Not Registered"));
    }
}

