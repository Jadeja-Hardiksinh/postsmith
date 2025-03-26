package com.learn.postsmith.controller.api.integrations.x;

import com.learn.postsmith.dto.GeneralResponseDTO;
import com.learn.postsmith.entity.UserDetail;
import com.learn.postsmith.service.UserDetailService;
import com.learn.postsmith.service.XService.XOauthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/user/x")
public class XIntegration {
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    XOauthService xOauthService;


    @GetMapping("/callback")
    public Mono<ResponseEntity<String>> callbackimpl(@RequestParam(value = "oauth_token") String oauthToken, @RequestParam(name = "oauth_verifier") String oauthVerifier) {
        return xOauthService.generateAccessToken(oauthToken, oauthVerifier);

    }

    @GetMapping("/token")
    public ResponseEntity<GeneralResponseDTO> getXAuth(HttpSession session) throws NoSuchAlgorithmException, InvalidKeyException {
        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            UserDetail user = userDetailService.findByEmail(authentication.getName());

            return xOauthService.generateRequestToken(user);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }
}
