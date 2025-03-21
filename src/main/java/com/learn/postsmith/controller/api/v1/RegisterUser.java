package com.learn.postsmith.controller.api.v1;

import com.learn.postsmith.dto.GeneralResponseDTO;
import com.learn.postsmith.dto.UserDetailDTO;
import com.learn.postsmith.entity.UserDetail;
import com.learn.postsmith.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class RegisterUser {

    @Autowired
    UserDetailService userDetailService;


    @PostMapping("/create")
    public ResponseEntity<GeneralResponseDTO> addUser(@RequestBody UserDetail user) {
        if (!userDetailService.userExist(user)) {
            userDetailService.createUser(user);
            return new ResponseEntity<>(new GeneralResponseDTO("success", "User registered Sucessfully", "/login", new UserDetailDTO(user.getId(), user.getFname(), user.getEmail())), HttpStatus.OK);


        } else {
            return new ResponseEntity<>(new GeneralResponseDTO("error", "User already Present", null, null), HttpStatus.OK);
        }

    }


}
