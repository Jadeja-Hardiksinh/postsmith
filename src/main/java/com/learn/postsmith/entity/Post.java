package com.learn.postsmith.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learn.postsmith.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Column(name = "reference_id")
    Long referenceId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Column(name = "media_url")
    private String mediaUrl;
    @Column(name = "media_name")
    private String mediaName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus postStatus;
    @ManyToOne()
    @JoinColumns({
            @JoinColumn(name = "platform", referencedColumnName = "platform"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    private Platform platform;

    public Post(String content, String mediaUrl, String mediaName, LocalDateTime scheduledAt, PostStatus postStatus, Platform platform) {
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.scheduledAt = scheduledAt;
        this.postStatus = postStatus;
        this.platform = platform;
        this.mediaName = mediaName;
    }
}
