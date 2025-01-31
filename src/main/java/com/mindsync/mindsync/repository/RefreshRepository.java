package com.mindsync.mindsync.repository;

import com.mindsync.mindsync.entity.Refresh;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends MongoRepository<Refresh, String> {
    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
