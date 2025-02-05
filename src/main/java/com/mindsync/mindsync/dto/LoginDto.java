package com.mindsync.mindsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String email;

    private String password;
}
