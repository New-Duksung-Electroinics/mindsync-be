package com.mindsync.mindsync.config;

import com.mindsync.mindsync.config.jwt.JwtAuthenticationFilter;
import com.mindsync.mindsync.config.jwt.JwtLoginFilter;
import com.mindsync.mindsync.config.jwt.JwtLogoutFilter;
import com.mindsync.mindsync.config.jwt.JwtUtil;
import com.mindsync.mindsync.repository.RefreshRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Value("${cors.allowed-origins}")
    private String allowedOriginsCsv;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
            JwtUtil jwtUtil,
            RefreshRepository refreshRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    // 경로 상수
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger", "/swagger-ui.html", "/swagger-ui/**",
            "/api-docs", "/api-docs/**", "/v3/api-docs/**"
    };
    private static final String[] AUTH_WHITELIST = {
            "/auth/login",            // 로그인 (필터 처리)
            "/auth/logout",           // 로그아웃 (필터 처리)
            "/auth/reissue",          // 액세스 토큰 재발급
            "/auth/token/validate"    // 토큰 유효성 검사
    };
    private static final String[] USER_WHITELIST = {
            "/users",                 // 회원가입 (POST)
            "/users/check-email"      // 이메일 중복 확인 (POST)
    };
    private static final String[] PUBLIC_WHITELIST = {
            "/ws-chat/**"             // STOMP/WebSocket 핸드셰이크
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Stateless
        http.csrf(csrf -> csrf.disable());
        http.formLogin(form -> form.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 권한 매칭
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .requestMatchers(PUBLIC_WHITELIST).permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(USER_WHITELIST).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 채팅/유저 보호 리소스
                .requestMatchers(
                        "/users/me/**",
                        "/chat/rooms/**"        // 방 생성/조회/요약/메시지 조회 등
                ).authenticated()
                .anyRequest().authenticated()
        );

        // 필터 체인
        http.addFilterAt(new JwtLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new JwtLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(Arrays.asList(allowedOriginsCsv.split("\\s*,\\s*")));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization","access","Set-Cookie","Content-Type"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }
}
