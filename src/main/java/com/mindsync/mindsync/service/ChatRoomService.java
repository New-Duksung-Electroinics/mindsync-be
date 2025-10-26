package com.mindsync.mindsync.service;

import com.mindsync.mindsync.document.ChatRoom;
import com.mindsync.mindsync.repository.ChatRoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) { // <-- public
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createRoom(String title, String host_email, List<String> participants, String content, String mbti) {
        ChatRoom chatRoom = ChatRoom.create(title, host_email, participants, content, mbti);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getSummaryByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 roomId가 없습니다."));
    }

    public ChatRoom getRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElse(null);
    }
}
