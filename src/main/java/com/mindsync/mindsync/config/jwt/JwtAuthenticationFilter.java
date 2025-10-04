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
import java.util.Set;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final Set<String> WHITELIST_EXACT = Set.of(

    );
    private static final String[] WHITELIST_PREFIX = new String[]{
            "/swagger", "/swagger-ui", "/api-docs", "/v3/api-docs",
            "/auth/login", "/auth/logout", "/auth/reissue", "/auth/token/validate",
            "/users",
            "/ws-chat"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (WHITELIST_EXACT.contains(path)) return true;
        for (String p : WHITELIST_PREFIX) {
            if (path.equals(p) || path.startsWith(p + "/")) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String accessToken = (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;

        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter w = response.getWriter()) { w.print("access token 만료"); }
            return;
        }

        if (!"access".equals(jwtUtil.getCategory(accessToken))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter w = response.getWriter()) { w.print("access token 불분명"); }
            return;
        }

        String email = jwtUtil.getEmail(accessToken);
        String role  = jwtUtil.getRole(accessToken);

        User user = new User();
        user.setEmail(email);
        user.setRole(role);

        CustomUserDetails cud = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(cud, null, cud.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(request, response);
    }
}
