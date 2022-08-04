package com.tw.login.user.controller;

import com.tw.login.user.Constants;
import com.tw.login.user.dto.LoginDto;
import com.tw.login.user.dto.RegisterDto;
import com.tw.login.user.dto.UserDto;
import com.tw.login.user.entity.User;
import com.tw.login.user.exception.PasswordIncorrectException;
import com.tw.login.user.exception.UserAlreadyPresentException;
import com.tw.login.user.exception.UserNotRegisteredException;
import com.tw.login.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        } catch (UserAlreadyPresentException exception) {
            return new ResponseEntity<>("Registration failed " + exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("login")
    public ResponseEntity<Map> login(@RequestBody LoginDto loginDto) throws UserNotRegisteredException, PasswordIncorrectException {
        HashMap<String,String> map = new HashMap<>();
        try {
            User user = modelMapper.map(loginDto, User.class);
            User savedUser = userService.validate(user);
            map.put("msg","Login Successful");
            map.put("token",generateJWTToken(savedUser));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (UserNotRegisteredException | PasswordIncorrectException exception) {
            map.put("msg","Login failed " + exception.getMessage());
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("profile")
    public ResponseEntity<UserDto> getUserDetails(HttpServletRequest httpServletRequest) throws UserNotRegisteredException {
        Long id=(Long) httpServletRequest.getAttribute("id");
        User user = userService.getDetails(id);
        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }

    private String generateJWTToken(User user){
        long currentTime=System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime+Constants.TOKEN_VALIDITY))
                .claim("id",user.getId())
                .claim("userName",user.getUserName())
                .compact();
        return token;
    }
}
