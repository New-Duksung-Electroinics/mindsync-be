package com.mindsync.mindsync.config.jwt;

import com.mindsync.mindsync.config.auth.CustomUserDetails;
import com.mindsync.mindsync.document.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/user/login") || path.equals("/user/join") || path.equals("/user/check-email");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String accessToken = null;

        if (header != null && header.startsWith("Bearer ")) {
            accessToken = header.substring(7); // "Bearer " 제거
        }

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e) {
             // response body
            PrintWriter writer = response.getWriter();
            writer.print("access token 만료");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;

        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            // response body
            PrintWriter writer = response.getWriter();
            writer.print("access token 불분명");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
