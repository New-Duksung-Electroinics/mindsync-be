package com.mindsync.mindsync.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindsync.mindsync.dto.ResponseDto;
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

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청 URI 확인
        if (!httpRequest.getRequestURI().equals("/api/user/logout")) {
            chain.doFilter(request, response);
            return;
        }

        // 요청 메소드 확인 (POST만 허용)
        if (!httpRequest.getMethod().equals("POST")) {
            ResponseDto<String> responseDto = ResponseUtil.ERROR("잘못된 요청입니다. 로그아웃은 POST 요청만 가능합니다.", null);
            writeJsonResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, responseDto);
            return;
        }

        // Refresh Token 가져오기
        String refresh = null;
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        // Refresh Token이 없으면 오류 응답 반환
        if (refresh == null) {
            ResponseDto<String> responseDto = ResponseUtil.ERROR("Refresh Token이 없습니다.", null);
            writeJsonResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, responseDto);
            return;
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            ResponseDto<String> responseDto = ResponseUtil.ERROR("Refresh Token이 만료되었습니다.", null);
            writeJsonResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, responseDto);
            return;
        }

        // 토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            ResponseDto<String> responseDto = ResponseUtil.ERROR("유효하지 않은 Refresh Token입니다.", null);
            writeJsonResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, responseDto);
            return;
        }

        // Refresh Token이 DB에 존재하는지 확인
        boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            ResponseDto<String> responseDto = ResponseUtil.ERROR("유효하지 않은 Refresh Token입니다.", null);
            writeJsonResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, responseDto);
            return;
        }

        // Refresh Token DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        // Refresh Token을 제거하는 쿠키 설정
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/api/user");
        httpResponse.addCookie(cookie);

        // 성공 응답 반환
        ResponseDto<String> responseDto = ResponseUtil.SUCCESS("로그아웃 되었습니다.", null);
        writeJsonResponse(httpResponse, HttpServletResponse.SC_OK, responseDto);
    }


    private void writeJsonResponse(HttpServletResponse response, int status, ResponseDto<?> responseDto) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), responseDto);
    }

}
