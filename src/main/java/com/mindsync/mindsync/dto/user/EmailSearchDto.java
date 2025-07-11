package com.mindsync.mindsync.dto.user;

import lombok.Getter;

@Getter
public class EmailSearchDto {
    private String email;

    public EmailSearchDto(String email) {
        this.email = email;
    }
}
