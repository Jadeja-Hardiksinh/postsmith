package com.learn.postsmith.repository;

import com.learn.postsmith.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    public Platform findByRequestToken(String request_token);
    
}
