package com.tw.login.user.controller;

import com.tw.login.user.dto.LoginDto;
import com.tw.login.user.dto.RegisterDto;
import com.tw.login.user.dto.UserDto;
import com.tw.login.user.entity.User;
import com.tw.login.user.exception.PasswordIncorrectException;
import com.tw.login.user.exception.UserAlreadyPresentException;
import com.tw.login.user.exception.UserNotRegisteredException;
import com.tw.login.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            User user = modelMapper.map(registerDto, User.class);
            userService.add(user);
            return new ResponseEntity<>("Registration successful", HttpStatus.CREATED);
        } catch (UserAlreadyPresentException e) {
            return new ResponseEntity<>("Registration failed " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) throws UserNotRegisteredException, PasswordIncorrectException {
        try {
            User user = modelMapper.map(loginDto, User.class);
            userService.validate(user);
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } catch (UserNotRegisteredException | PasswordIncorrectException e) {
            return new ResponseEntity<>("Login failed " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("profile/{userName}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable String userName) throws UserNotRegisteredException {
        User user = userService.getDetails(userName);
        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }
}
