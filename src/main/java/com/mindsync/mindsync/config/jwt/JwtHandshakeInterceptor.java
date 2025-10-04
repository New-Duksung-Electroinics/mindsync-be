package com.mindsync.mindsync.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("🔥 WebSocket 핸드셰이크 시작");

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            String token = Optional.ofNullable(httpServletRequest.getParameter("token"))
                    .orElseGet(() -> {
                        String header = httpServletRequest.getHeader("Authorization");
                        return header != null ? header.replace("Bearer ", "") : null;
                    });

            System.out.println("🟡 추출된 AccessToken: " + token);

            if (token == null || !jwtUtil.validateToken(token)) {
                System.out.println("🔴 WebSocket 연결 실패: 유효하지 않은 AccessToken");
                return false;
            }

            String email = jwtUtil.getEmail(token);
            attributes.put("user", email);
            System.out.println("✅ WebSocket 연결 성공: " + email);
            return true;
        }

        System.out.println("🔴 WebSocket 요청이 ServletServerHttpRequest가 아님");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}