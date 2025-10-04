package com.mindsync.mindsync.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindsync.mindsync.dto.response.CommonResponse;
import com.mindsync.mindsync.repository.RefreshRepository;
import com.mindsync.mindsync.utils.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

public class JwtLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtLogoutFilter(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest httpReq) || !(res instanceof HttpServletResponse httpRes)) {
            chain.doFilter(req, res);
            return;
        }

        if (!httpReq.getRequestURI().equals("/auth/logout")) {
            chain.doFilter(req, res);
            return;
        }

        if (!httpReq.getMethod().equals("POST")) {
            writeJson(httpRes, HttpServletResponse.SC_BAD_REQUEST, ResponseUtil.ERROR("잘못된 요청입니다. 로그아웃은 POST 요청만 가능합니다.", null));
            return;
        }

        String refresh = null;
        Cookie[] cookies = httpReq.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) if ("refresh".equals(c.getName())) refresh = c.getValue();
        }
        if (refresh == null) {
            writeJson(httpRes, HttpServletResponse.SC_BAD_REQUEST, ResponseUtil.ERROR("Refresh Token이 없습니다.", null));
            return;
        }

        try { jwtUtil.isExpired(refresh); }
        catch (ExpiredJwtException e) {
            writeJson(httpRes, HttpServletResponse.SC_BAD_REQUEST, ResponseUtil.ERROR("Refresh Token이 만료되었습니다.", null));
            return;
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            writeJson(httpRes, HttpServletResponse.SC_BAD_REQUEST, ResponseUtil.ERROR("유효하지 않은 Refresh Token입니다.", null));
            return;
        }

        if (!refreshRepository.existsByRefresh(refresh)) {
            writeJson(httpRes, HttpServletResponse.SC_BAD_REQUEST, ResponseUtil.ERROR("유효하지 않은 Refresh Token입니다.", null));
            return;
        }

        refreshRepository.deleteByRefresh(refresh);

        // 쿠키 제거 (경로는 넓게 설정)
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        httpRes.addCookie(cookie);

        writeJson(httpRes, HttpServletResponse.SC_OK, ResponseUtil.SUCCESS("로그아웃 되었습니다.", null));
    }

    private void writeJson(HttpServletResponse res, int status, CommonResponse<?> body) throws IOException {
        res.setStatus(status);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), body);
    }
}
