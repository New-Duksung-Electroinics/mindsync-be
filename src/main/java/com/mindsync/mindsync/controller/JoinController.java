package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.EmailCheckDto;
import com.mindsync.mindsync.dto.JoinDto;
import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.service.JoinService;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User API", description = "사용자 API")
@RequestMapping("/user")
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    // 회원가입 API
    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "중복된 회원을 검사하고 회원가입이 완료됩니다.")
    public ResponseDto joinProcess(@RequestBody JoinDto joinDto) {
        try {
            joinService.joinProcess(joinDto);
            return ResponseUtil.SUCCESS("회원가입이 완료되었습니다", null);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.ERROR("이미 가입된 사용자입니다.", null);
        } catch (Exception e) {
            return ResponseUtil.ERROR("서버 에러가 발생했습니다", null);
        }
    }

    // 이메일 중복 검사 API
    @PostMapping("/check-email")
    @Operation(summary = "회원가입", description = "중복된 회원을 검사합니다.")
    public ResponseDto<String> checkEmail(@RequestBody EmailCheckDto emailCheckDTO) {
        boolean exists = joinService.isEmailExist(emailCheckDTO.getEmail());
        if (exists) {
            return ResponseUtil.ERROR("이미 가입된 사용자입니다.", null);
        } else {
            return ResponseUtil.SUCCESS("사용 가능한 이메일입니다.", null);
        }

    }
}
