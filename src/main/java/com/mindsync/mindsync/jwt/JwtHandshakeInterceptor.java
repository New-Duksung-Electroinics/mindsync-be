package com.mindsync.mindsync.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    public JwtHandshakeInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            String token = Optional.ofNullable(httpServletRequest.getHeader("Authorization"))
                    .map(t -> t.replace("Bearer ", ""))
                    .orElse(null);

            if (token == null) {
                System.out.println("🔴 WebSocket 연결 실패: AccessToken 없음");
                return false;
            }

            if (!jwtUtil.validateToken(token)) {
                System.out.println("🔴 WebSocket 연결 실패: 유효하지 않은 AccessToken");
                return false;
            }

            String email = jwtUtil.getEmail(token);
            attributes.put("user", email);
            System.out.println("🟢 WebSocket 연결 성공: " + email);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
