package com.mindsync.mindsync.repository;

import com.mindsync.mindsync.entity.ChatRoomMessages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatRoomMessages, String> {
}
