package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.CustomUserDetails;
import com.mindsync.mindsync.entity.User;
import com.mindsync.mindsync.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(username);

        if (userData == null) {
            throw new UsernameNotFoundException("User not found with email: " + username); // 반드시 예외 던지기
        }

        return new CustomUserDetails(userData);
    }

}
