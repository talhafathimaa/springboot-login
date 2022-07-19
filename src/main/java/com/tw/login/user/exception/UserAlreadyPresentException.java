package com.tw.login.user.exception;

public class UserAlreadyPresentException extends Exception {
    public UserAlreadyPresentException(String message) {
        super(message);
    }
}
