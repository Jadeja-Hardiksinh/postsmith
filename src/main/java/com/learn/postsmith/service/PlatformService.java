package com.learn.postsmith.service;

import com.learn.postsmith.entity.Platform;
import com.learn.postsmith.enums.PlatformName;
import com.learn.postsmith.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformService {
    @Autowired
    PlatformRepository repository;

    public boolean addPlatformRecord(Platform platform) {
        Platform savedPlatform = repository.save(platform);
        return savedPlatform.getId() != null;

    }

    public Platform findPlatformByRequestToken(String request_token) {
        return repository.findByRequestToken(request_token);

    }

    public Platform findPlatformByUserIdAndPlatformName(Long userId, PlatformName name) {
        return repository.findByUserIdAndPlatform(userId, name);

    }


}
