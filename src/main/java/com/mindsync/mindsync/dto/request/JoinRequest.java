package com.mindsync.mindsync.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequest {

    private String email;
    private String password;
    private String usermbti;
    private String username;
}
