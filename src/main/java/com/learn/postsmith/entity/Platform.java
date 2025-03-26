package com.learn.postsmith.entity;

import com.learn.postsmith.enums.PlatformName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platform_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "request_token")
    String requestToken;
    @Column(name = "access_token")

    String accessToken;
    @Column(name = "token_secret")

    String tokenSecret;
    @Enumerated(value = EnumType.STRING)
    PlatformName platform;

    public Platform(Long userId, PlatformName platform) {
        this.userId = userId;
        this.platform = platform;
    }
}
