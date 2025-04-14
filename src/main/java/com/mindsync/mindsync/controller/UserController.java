package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.CustomUserDetails;
import com.mindsync.mindsync.dto.MbtiUpdateDto;
import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.service.UserService;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/mbti")
    @Operation(summary = "MBTI 수정", description = "나의 MBTI를 수정합니다.")
    public ResponseDto<?> updateMbti(@RequestBody MbtiUpdateDto dto,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            userService.updateMbti(userDetails.getUsername(), dto.getUsermbti());
            return ResponseUtil.SUCCESS("mbti 수정을 완료했습니다.", null);
        } catch (Exception e) {
            return ResponseUtil.ERROR("서버 에러가 발생했습니다.", null);
        }
    }
}
