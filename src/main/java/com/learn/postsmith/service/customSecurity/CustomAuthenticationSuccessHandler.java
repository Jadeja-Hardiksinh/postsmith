package com.learn.postsmith.service.customSecurity;

import com.learn.postsmith.entity.UserDetail;
import com.learn.postsmith.service.UserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserDetailService userDetailService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userEmail = authentication.getName();
        UserDetail user = userDetailService.findByEmail(userEmail);
        HttpSession session = request.getSession(false);
        session.setAttribute("userId", user.getId());
        response.sendRedirect("/dashboard");
    }
}
