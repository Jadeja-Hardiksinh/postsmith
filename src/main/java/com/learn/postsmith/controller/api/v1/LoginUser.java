package com.learn.postsmith.controller.api.v1;

import com.learn.postsmith.dto.GeneralResponseDTO;
import com.learn.postsmith.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class LoginUser {
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<GeneralResponseDTO> userLogin(@RequestBody UserDetail user) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        try {
            authentication = authenticationManager.authenticate(authentication);

            return new ResponseEntity<>(new GeneralResponseDTO("success", "User authenticated", null, null), HttpStatus.OK);

        } catch (UsernameNotFoundException | BadCredentialsException e) {

            return new ResponseEntity<>(new GeneralResponseDTO("error", e.getMessage(), null, null), HttpStatus.OK);
        }


    }
}
