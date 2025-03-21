package com.learn.postsmith.repository;

import com.learn.postsmith.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    UserDetail findByEmail(String email);

}
