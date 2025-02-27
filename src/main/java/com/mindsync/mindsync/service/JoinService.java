package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.JoinDto;
import com.mindsync.mindsync.entity.User;
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

    public void joinProcess(JoinDto joinDto) {
        String email = joinDto.getEmail();
        String password = joinDto.getPassword();
        String username = joinDto.getUsername();
        String usermbti = joinDto.getUsermbti();

        Boolean isExist = userRepository.existsByEmail(email);

        // 이미 가입된 회원이면
        if (isExist) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        try{
            User data = new User();
            data.setEmail(email);
            data.setUsername(username);
            data.setUsermbti(usermbti);
            data.setPassword(bCryptPasswordEncoder.encode(password));
            data.setRole("ROLE_USER");

            userRepository.save(data);
        }catch (Exception e) {
            throw new RuntimeException("서버 오류가 발생했습니다.");
        }

    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

}
