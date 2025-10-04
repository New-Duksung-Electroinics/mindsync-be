package com.mindsync.mindsync.controller.users;

import com.mindsync.mindsync.config.auth.CustomUserDetails;
import com.mindsync.mindsync.dto.request.EmailCheckRequest;
import com.mindsync.mindsync.dto.request.JoinRequest;
import com.mindsync.mindsync.dto.request.MbtiUpdateRequest;
import com.mindsync.mindsync.dto.response.CommonResponse;
import com.mindsync.mindsync.service.JoinService;
import com.mindsync.mindsync.service.UserService;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final JoinService joinService;
    private final UserService userService;

    public UserController(JoinService joinService, UserService userService) {
        this.joinService = joinService;
        this.userService = userService;
    }

    // 회원가입 (기존 /user/join)
    @PostMapping
    @Operation(summary = "회원가입")
    public ResponseEntity<CommonResponse<Void>> create(@RequestBody JoinRequest dto) {
        try {
            joinService.joinProcess(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.SUCCESS("회원가입이 완료되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseUtil.ERROR("이미 가입된 사용자입니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.ERROR("서버 에러가 발생했습니다.", null));
        }
    }

    // 이메일 중복 체크 (기존 /user/check-email)
    @PostMapping("/check-email")
    @Operation(summary = "이메일 중복 확인")
    public ResponseEntity<CommonResponse<Void>> checkEmail(@RequestBody EmailCheckRequest dto) {
        return joinService.isEmailExist(dto.getEmail())
                ? ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseUtil.ERROR("이미 가입된 사용자입니다.", null))
                : ResponseEntity.ok(ResponseUtil.SUCCESS("사용 가능한 이메일입니다.", null));
    }

    // MBTI 수정 (기존 /user/mbti)
    @PutMapping("/me/mbti")
    @Operation(summary = "내 MBTI 수정")
    public CommonResponse<Void> updateMbti(@RequestBody MbtiUpdateRequest dto,
            @AuthenticationPrincipal CustomUserDetails user) {
        try {
            userService.updateMbti(user.getUsername(), dto.getUsermbti());
            return ResponseUtil.SUCCESS("MBTI 수정을 완료했습니다.", null);
        } catch (Exception e) {
            return ResponseUtil.ERROR("서버 에러가 발생했습니다.", null);
        }
    }
}
