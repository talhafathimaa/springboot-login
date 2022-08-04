package com.tw.login.user.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDto {
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$", message = "Username should only contain alphabets, numbers and characters(_,.,-)")
    @NotBlank(message = "user name cannot be empty")
    private String userName;

    @NotEmpty(message = "password cannot be empty")
    @Pattern(regexp = "^[?!\\S]*$", message = "password cannot include spaces")
    @Size(min = 6, message = "Password should have at-least 6 characters")
    private String password;

    @NotBlank(message = "first name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "First name should only contain alphabets")
    private String firstName;

    @NotBlank(message = "last name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Last name should only contain alphabets")
    private String lastName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public RegisterDto(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
