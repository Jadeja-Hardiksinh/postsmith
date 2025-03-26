package com.learn.postsmith.service;

import com.learn.postsmith.entity.Post;
import com.learn.postsmith.enums.PostStatus;
import com.learn.postsmith.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HandlePostService {
    @Autowired
    private PostRepository repository;

    public boolean addPost(Post post) {
        return repository.save(post).getId() != null;
    }

    public Post addPostAndReturnObj(Post post) {
        return repository.save(post);
    }

    public Optional<Post> findPostById(Long id) {
        return repository.findById(id);
    }

    public List<Post> findPostsByStatus(PostStatus status) {
        return repository.findAllByPostStatus(status, Sort.by(Sort.Direction.ASC, "scheduledAt"));

    }
}
