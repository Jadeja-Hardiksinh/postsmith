package com.learn.postsmith.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learn.postsmith.enums.PlatformName;
import com.learn.postsmith.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    Long Id;
    PlatformName platform;
    String content;
    MultipartFile media;
    PostStatus status;
    LocalDateTime schedule;
    Long userId;
    Long referenceId;


}
