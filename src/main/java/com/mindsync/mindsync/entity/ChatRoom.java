package com.mindsync.mindsync.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Document(collection = "chatroom")
public class ChatRoom {

    @Id
    private String id;
    private String roomId;
    private String title; // 회의 제목
    private String host_email; // 회의 주최자 이메일
    private List<String> participants; // 참여 멤버 (이메일 리스트)
    private String content; // 회의 예상 내용
    private String mbti; // 헬퍼 mbti

    @Builder
    public ChatRoom(String id, String roomId, String title, String host_email, List<String> participants, String content, String mbti) {
        this.id = id;
        this.roomId = roomId;
        this.title = title;
        this.host_email = host_email;
        this.participants = participants;
        this.content = content;
        this.mbti = mbti;
    }

    public static ChatRoom create(String title, String host_email, List<String> participants, String content, String mbti) {
        return ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .roomId(UUID.randomUUID().toString()) // 채팅방의 고유 ID
                .title(title)
                .host_email(host_email)
                .participants(participants)
                .content(content)
                .mbti(mbti)
                .build();
    }

}
