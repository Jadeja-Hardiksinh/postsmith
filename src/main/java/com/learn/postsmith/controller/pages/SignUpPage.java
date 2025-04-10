package com.learn.postsmith.controller.pages;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class SignUpPage {
    @GetMapping
    public ResponseEntity<String> renderSignUpPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/dashboard");
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/html/signup.html"));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                htmlContent.append(line);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html");
            return new ResponseEntity<String>(htmlContent.toString(), headers, HttpStatus.OK);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
