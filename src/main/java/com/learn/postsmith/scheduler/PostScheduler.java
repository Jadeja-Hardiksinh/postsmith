package com.learn.postsmith.scheduler;

import com.learn.postsmith.entity.Post;
import com.learn.postsmith.enums.PostStatus;
import com.learn.postsmith.service.HandlePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class PostScheduler {
    @Autowired
    HandlePostService postService;

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    public void printTime() {
        List<Post> pendingPosts = postService.findPostsByStatus(PostStatus.PENDING);
        pendingPosts.forEach(post -> {
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

            System.out.println("Now time : " + now.toString());
            System.out.println("Post time : " + post.getScheduledAt().toString());
            if (post.getScheduledAt().isEqual(now)) {
                System.out.println(post.getId());
                System.out.println(true);
            } else {
                System.out.println(false);
            }
        });

        System.out.println("--------------------------------------------");
    }

}
