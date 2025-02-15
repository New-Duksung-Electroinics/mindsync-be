package com.mindsync.mindsync.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

public class JwtHandshakeIntercepter implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    public JwtHandshakeIntercepter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes){
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            String token = Optional.ofNullable(httpServletRequest.getHeader("Authorization"))
                    .map(t -> t.replace("Bearer ", ""))
                    .orElse(null);

            if (token != null && jwtUtil.validateToken(token)) {

            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private final JWTUtil
}
