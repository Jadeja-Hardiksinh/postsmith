package com.learn.postsmith.dto;

import lombok.Getter;

@Getter
public class UserDetailDTO {
    private Long id;
    private String fname;
    private String email;


    public UserDetailDTO(Long id, String fname, String email) {
        this.id = id;
        this.email = email;
        this.fname = fname;

    }
}
