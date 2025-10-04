package com.mindsync.mindsync.controller.auth;

import com.mindsync.mindsync.config.jwt.JwtUtil;
import com.mindsync.mindsync.dto.response.CommonResponse;
import com.mindsync.mindsync.repository.RefreshRepository;
import com.mindsync.mindsync.utils.ResponseStatus;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public AuthController(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    // 로그인은 Security Filter에서 처리됨 (경로만 제공)
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "Spring Security 필터에서 처리됩니다.")
    public CommonResponse<Void> login() {
        return ResponseUtil.ERROR("Spring Security 필터에서 처리됩니다.", null);
    }

    // 로그아웃도 필터에서 처리
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Spring Security 필터에서 처리됩니다.")
    public CommonResponse<Void> logout() {
        return ResponseUtil.ERROR("Spring Security 필터에서 처리됩니다.", null);
    }

    // 재발급 (기존 ReissueController 로직 이관)
    @PostMapping("/reissue")
    @Operation(summary = "액세스 토큰 재발급", description = "Refresh Token으로 새 토큰을 발급합니다.")
    public CommonResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) if ("refresh".equals(c.getName())) refresh = c.getValue();
        }
        if (refresh == null) return ResponseUtil.ERROR("Refresh Token이 없습니다.", null);

        try { jwtUtil.isExpired(refresh); }
        catch (ExpiredJwtException e) { return ResponseUtil.ERROR("Refresh Token이 만료되었습니다.", null); }

        if (!"refresh".equals(jwtUtil.getCategory(refresh)))
            return ResponseUtil.ERROR("Refresh Token이 유효하지 않습니다.", null);

        if (!refreshRepository.existsByRefresh(refresh))
            return ResponseUtil.ERROR("Refresh Token이 DB에 없습니다.", null);

        String email = jwtUtil.getEmail(refresh);
        String role  = jwtUtil.getRole(refresh);

        String newAccess  = jwtUtil.createJwt("access",  email, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        // 저장/쿠키 로직은 기존 메서드 그대로 호출(생략)
        response.setHeader("access", newAccess);
        Cookie cookie = new Cookie("refresh", newRefresh);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24*60*60);
        response.addCookie(cookie);

        return ResponseUtil.SUCCESS("토큰 재발급 완료", null);
    }

    // 토큰 유효성 검사 (기존 TokenController 이동)
    @GetMapping("/token/isValid")
    public CommonResponse<Boolean> validate(@RequestHeader(value = "Authorization", required = false) String header) {
        if (header == null || !header.startsWith("Bearer "))
            return new CommonResponse<>(ResponseStatus.FAILURE, "Authorization header 오류", false);
        String token = header.substring(7);
        try { return new CommonResponse<>(ResponseStatus.SUCCESS, "유효", !jwtUtil.isExpired(token)); }
        catch (Exception e) { return new CommonResponse<>(ResponseStatus.SUCCESS, "무효", false); }
    }
}
