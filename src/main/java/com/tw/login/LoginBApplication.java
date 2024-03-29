package com.tw.login;

import com.tw.login.filters.AuthorizationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class LoginBApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginBApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public FilterRegistrationBean<AuthorizationFilter> filterFilterRegistrationBean(){
        FilterRegistrationBean<AuthorizationFilter> registrationBean=new FilterRegistrationBean<>();
        AuthorizationFilter authorizationFilter=new AuthorizationFilter();
        registrationBean.setFilter(authorizationFilter);
        registrationBean.addUrlPatterns("/api/profile/*");
        return registrationBean;
    }
}
