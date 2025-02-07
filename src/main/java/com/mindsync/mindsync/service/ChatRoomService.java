package com.mindsync.mindsync.service;

import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor // 생성자 주입
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;


    public ChatRoom createRoom(String title, String host, List<String> participants, String content) {
        // 임의의 회의 목차 생성 (AI 연동 전 테스트용)
        List<String> agenda = Arrays.asList(
                "1. " + title + " 개요",
                "2. 주요 논의 사항",
                "3. 진행 상황 점검",
                "4. 문제 해결 방안",
                "5. 최종 결정 사항"
        );

        // 채팅방 생성 및 저장
        ChatRoom chatRoom = ChatRoom.create(title, host, participants, content, agenda);
        return chatRoomRepository.save(chatRoom);
    }

}
