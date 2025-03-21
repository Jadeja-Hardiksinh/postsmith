package com.learn.postsmith.service;

import com.learn.postsmith.entity.UserDetail;
import com.learn.postsmith.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserDetailRepository userDetailRepository;

    public void createUser(UserDetail user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDetailRepository.save(user);
    }

    public boolean userExist(UserDetail user) {
        if (userDetailRepository.findByEmail(user.getEmail()) != null) {
            return true;
        } else {
            return false;

        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetail user = userDetailRepository.findByEmail(email);
        if (user != null) {
            return User.builder().username(user.getEmail()).password(user.getPassword()).roles(user.getUserRole().name()).build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
