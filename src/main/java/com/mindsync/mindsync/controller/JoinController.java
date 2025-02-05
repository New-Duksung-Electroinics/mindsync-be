package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.JoinDTO;
import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.service.JoinService;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User API", description = "사용자 API")
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    // 회원가입 API
    @PostMapping("/user/join")
    @Operation(summary = "회원가입", description = "중복된 회원을 검사하고 회원가입이 완료됩니다.")
    public ResponseDto joinProcess(@RequestBody JoinDTO joinDTO) {
        try {
            joinService.joinProcess(joinDTO);
            return ResponseUtil.SUCCESS("회원가입이 완료되었습니다", null);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.ERROR("서버 에러가 발생했습니다.", null);
        }

    }
}
