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

        // 채팅방 생성 및 저장
        ChatRoom chatRoom = ChatRoom.create(title, host, participants, content);
        return chatRoomRepository.save(chatRoom);
    }

}
