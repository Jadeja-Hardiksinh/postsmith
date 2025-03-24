package com.learn.postsmith.controller.api.integrations.x;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/x")
public class XIntegration {
    @GetMapping("/auth")
    public ResponseEntity<String> getXAuth() {
        String requestTokenUrl = "https://api.twitter.com/oauth/request_token";
    }
}
