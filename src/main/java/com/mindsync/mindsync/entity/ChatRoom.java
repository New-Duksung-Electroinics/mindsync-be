package com.mindsync.mindsync.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chatroom")
public class ChatRoom {

    @Id
    private String id;

    private String roomId;
    private String title; // 회의 제목
    private String host; // 회의 주최자 이름
    private List<String> participants; // 참여 멤버 (이메일 리스트)
    private String content; // 회의 예상 내용

    public static ChatRoom create(String title, String host, List<String> participants, String content) {
        return ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .roomId(UUID.randomUUID().toString()) // 채팅방의 고유 ID
                .title(title)
                .host(host)
                .participants(participants)
                .content(content)
                .build();
    }

}
