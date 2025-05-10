package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.jwt.JWTUtil;
import com.mindsync.mindsync.utils.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final JWTUtil jwtUtil;

    public TokenController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/isValid")
    public ResponseDto<Boolean> isTokenExpired(@RequestHeader("Authorization") String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return new ResponseDto<>(ResponseStatus.FAILURE, "Authorization header가 없거나 잘못되었습니다.", false);
        }
        String token = header.substring(7);
        boolean isValid;
        try {
            boolean expired = jwtUtil.isExpired(token);
            isValid = !expired;
        } catch (Exception e) {
            isValid = false;
        }
        return new ResponseDto<>(ResponseStatus.SUCCESS, "토큰 유효성 검사 완료되었습니다.", isValid);
    }
}
