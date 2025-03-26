package com.learn.postsmith.repository;


import com.learn.postsmith.entity.Post;
import com.learn.postsmith.enums.PostStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findAllByPostStatus(PostStatus status, Sort sort);

}
