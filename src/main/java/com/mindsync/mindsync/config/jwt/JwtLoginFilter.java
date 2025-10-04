package com.mindsync.mindsync.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindsync.mindsync.config.auth.CustomUserDetails;
import com.mindsync.mindsync.dto.response.CommonResponse;
import com.mindsync.mindsync.document.Refresh;
import com.mindsync.mindsync.repository.RefreshRepository;
import com.mindsync.mindsync.utils.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;

        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> body = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String email = body.get("email");
            String password = body.get("password");
            if (email == null || password == null) {
                throw new AuthenticationException("Invalid login request") {};
            }
            var token = new UsernamePasswordAuthenticationToken(email, password, null);
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            throw new AuthenticationException("Failed to parse JSON request") {};
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        String email = authentication.getName();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getRealName();
        String useremail = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> it = authorities.iterator();
        String role = it.hasNext() ? it.next().getAuthority() : "ROLE_USER";

        String access  = jwtUtil.createJwt("access",  email, role, 36000000L);
        String refresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        addRefreshEntity(email, refresh, 86400000L);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        CommonResponse<Map<String, String>> res =
                ResponseUtil.SUCCESS("로그인 성공했습니다.", Map.of("username", username, "useremail", useremail));
        writeJsonResponse(response, res);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        CommonResponse<String> res = ResponseUtil.ERROR("아이디 또는 비밀번호가 올바르지 않습니다.", null);
        writeJsonResponse(response, res);
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh entity = new Refresh();
        entity.setEmail(email);
        entity.setRefresh(refresh);
        entity.setExpiration(date.toString());
        refreshRepository.save(entity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);               // 배포 시 HTTPS에서 활성화 권장
        // cookie.setPath("/");                  // 필요 시 지정 (기본은 현재 경로)
        cookie.setMaxAge(24 * 60 * 60);
        return cookie;
    }

    private void writeJsonResponse(HttpServletResponse response, CommonResponse<?> body) throws IOException {
        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, body);
        writer.flush();
    }
}
