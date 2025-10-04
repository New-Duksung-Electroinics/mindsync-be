package com.mindsync.mindsync.dto.response;

import lombok.Getter;

@Getter
public class EmailSearchResponse {
    private String email;

    public EmailSearchResponse(String email) {
        this.email = email;
    }
}
