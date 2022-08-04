package com.tw.login.filters;

import com.tw.login.user.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest=(HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse=(HttpServletResponse) servletResponse;
        String authorizationHeader=httpRequest.getHeader("Authorization");
        if(authorizationHeader!=null) {
            String[] authorizationHeaderArray = authorizationHeader.split("Bearer ");
            if (authorizationHeaderArray.length > 1 && authorizationHeaderArray[1] != null) {
                String token = authorizationHeaderArray[1];
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY).parseClaimsJws(token).getBody();
                    httpRequest.setAttribute("id",Long.parseLong(claims.get("id").toString()));
                } catch (Exception exception) {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(),"Invalid or expired token");
                    return;
                }
            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization header should be in the format Bearer [token]");
                return;
            }
        }
                else{
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Please provide the authorization token");
                return;
                }
filterChain.doFilter(servletRequest,servletResponse);

    }
}
