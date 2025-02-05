package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "사용자 API")
public class LogoutController {
    @Operation(summary = "로그아웃 API", description = "Refresh Token 검증해 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseDto<String> logout(HttpServletResponse response) {
        return ResponseUtil.ERROR("Spring Security 필터에서 로그아웃이 처리됩니다.", null);
    }
}
