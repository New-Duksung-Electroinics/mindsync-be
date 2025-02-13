package com.mindsync.mindsync.service;

import com.mindsync.mindsync.entity.User;
import com.mindsync.mindsync.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseDto
}
