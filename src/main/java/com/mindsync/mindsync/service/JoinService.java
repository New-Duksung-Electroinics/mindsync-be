package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.request.JoinRequest;
import com.mindsync.mindsync.document.User;
import com.mindsync.mindsync.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinRequest joinRequest) {
        String email = joinRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }
        User data = new User();
        data.setEmail(email);
        data.setUsername(joinRequest.getUsername());
        data.setUsermbti(joinRequest.getUsermbti());
        data.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        data.setRole("ROLE_USER");
        userRepository.save(data);
    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}

