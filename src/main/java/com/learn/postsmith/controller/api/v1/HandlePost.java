package com.learn.postsmith.controller.api.v1;

import com.learn.postsmith.dto.PostDTO;
import com.learn.postsmith.entity.Platform;
import com.learn.postsmith.entity.Post;
import com.learn.postsmith.service.HandlePostService;
import com.learn.postsmith.service.PlatformService;
import com.learn.postsmith.service.UserDetailService;
import com.learn.postsmith.service.XService.XCreateTweetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController()
@RequestMapping("/api/v1/post")
public class HandlePost {
    @Autowired
    HandlePostService handlePostService;
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    XCreateTweetService tweetService;
    @Autowired
    PlatformService platformService;

    @PostMapping(value = "/create")
    public String createPost(@ModelAttribute PostDTO postDTO, HttpSession session) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
        postDTO.setUserId(userId);
        handlePostService.addFile(postDTO.getMedia());
        Platform platform = platformService.findPlatformByUserIdAndPlatformName(postDTO.getUserId(), postDTO.getPlatform());
        Post post = new Post(postDTO.getContent(), postDTO.getMedia().getOriginalFilename(), postDTO.getMedia().getName(), postDTO.getSchedule(), postDTO.getStatus(), platform);
        if (handlePostService.addPost(post)) {
            if (post.getPlatform().getPlatform().name().equals("X")) {
//                boolean result = tweetService.createTextTweet(post);
//                return String.valueOf(result);
                return String.valueOf(true);
            }
        }
        return "something went wrong while saving the post";
    }
}
