package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성 API (AI가 목차 생성 후 반환)
    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        String host = (String) request.get("host");
        List<String> participants = (List<String>) request.get("participants");
        String expectedContent = (String) request.get("content");

        ChatRoom chatRoom = chatRoomService.createRoom(title, host, participants, expectedContent);

        return ResponseEntity.ok(chatRoom);
    }

}
