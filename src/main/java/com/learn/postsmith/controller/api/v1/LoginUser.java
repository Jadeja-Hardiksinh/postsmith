package com.learn.postsmith.controller.api.v1;

import com.learn.postsmith.entity.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class LoginUser {


    @PostMapping("/login")
    public void userLogin(@RequestBody UserDetail user, HttpServletRequest request) {


    }
}
