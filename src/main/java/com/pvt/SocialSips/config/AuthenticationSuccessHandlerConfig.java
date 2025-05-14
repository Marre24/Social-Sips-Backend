package com.pvt.SocialSips.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerConfig implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                        Authentication authentication) throws IOException {
       if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS"))
           httpResponse.setStatus(200);
        httpResponse.sendRedirect("/user/profile");
    }
}

