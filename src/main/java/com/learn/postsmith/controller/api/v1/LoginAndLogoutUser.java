package com.learn.postsmith.controller.api.v1;

import com.learn.postsmith.entity.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class LoginAndLogoutUser {


    @PostMapping("/login")
    public void userLogin(@RequestBody UserDetail user, HttpSession session) {
        session.setAttribute("userId", 123);

    }

    @PostMapping("/logout")
    public void userLogout(@RequestBody UserDetail user, HttpServletRequest request) {


    }
}
