package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.response.EmailSearchResponse;
import com.mindsync.mindsync.document.User;
import com.mindsync.mindsync.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final int MAX_SUGGESTION = 20;

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<EmailSearchResponse> searchUsersByEmail(String query) {
        if (query == null || query.isBlank()) return List.of();

        List<User> users = userRepository.findByEmailStartingWithIgnoreCase(query);
        if (users.size() > MAX_SUGGESTION) {
            users = users.subList(0, MAX_SUGGESTION);
        }

        return users.stream()
                .map(u -> new EmailSearchResponse(u.getEmail()))
                .collect(Collectors.toList());
    }

    public void updateMbti(String email, String newMbti) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        user.setUsermbti(newMbti);
        userRepository.save(user);
    }
}

