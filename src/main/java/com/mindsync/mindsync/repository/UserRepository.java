package com.mindsync.mindsync.repository;

import com.mindsync.mindsync.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // 회원 중복 확인
    Boolean existsByEmail(String email);

    User findByEmail(String email);

    List<User> findByEmailStartingWith(String query);
}
