package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.JoinDTO;
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

    public void joinProcess(JoinDTO joinDTO) {
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();
        String nickname = joinDTO.getNickname();
        String usermbti = joinDTO.getUsermbti();

        Boolean isExist = userRepository.existsByEmail(email);

        // 이미 가입된 회원이면
        if (isExist) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        try{
            User data = new User();
            data.setEmail(email);
            data.setNickname(nickname);
            data.setUsermbti(usermbti);
            data.setPassword(bCryptPasswordEncoder.encode(password));
            data.setRole("ROLE_USER");

            userRepository.save(data);
        }catch (Exception e) {
            throw new RuntimeException("서버 오류가 발생했습니다.");
        }

    }

}
