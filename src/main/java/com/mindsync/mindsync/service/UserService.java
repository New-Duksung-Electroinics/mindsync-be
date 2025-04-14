package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.EmailSearchDto;
import com.mindsync.mindsync.entity.User;
import com.mindsync.mindsync.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<EmailSearchDto> searchUsersByEmail(String query) {
        List<User> users = userRepository.findByEmailStartingWith(query);

        return users.stream()
                .map(user -> new EmailSearchDto(user.getEmail()))
                .collect(Collectors.toList());
    }

    public void updateMbti(String email, String newMbti) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");

        user.setUsermbti(newMbti);
        userRepository.save(user);
    }
}
