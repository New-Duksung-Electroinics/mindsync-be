package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.LoginDto;
import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/user")
//@Tag(name = "User API", description = "사용자 API")
//public class LoginController {
//
//    @PostMapping("/login")
//    @Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호를 입력받아 로그인 처리합니다.")
//    public ResponseDto login(@RequestBody LoginDto loginDto) {
//        // 실제 로그인 로직은 Security Filter에서 처리됨
//        return ResponseUtil.ERROR("Spring Security 필터에서 처리됩니다.", null);
//    }
//}
