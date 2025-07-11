package com.mindsync.mindsync.service.chatroom;

import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.repository.ChatRoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createRoom(String title, String host_email, List<String> participants, String content, String mbti) {

        // 채팅방 생성 및 저장
        ChatRoom chatRoom = ChatRoom.create(title, host_email, participants, content, mbti);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getSummaryByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 roomId가 없습니다."));
    }

}
