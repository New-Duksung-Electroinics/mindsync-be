package com.mindsync.mindsync.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat")
public class ChatMessage {
    @Id
    private String roomId;

    // 목차 별로 그룹화 시키기 위해
    private Map<Integer, List<Message>> messages;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Message{
        private String sender;
        private String message;
        private LocalDateTime timestamp;
    }

}
