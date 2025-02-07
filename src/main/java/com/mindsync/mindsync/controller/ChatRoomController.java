package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ChatRoomRequestDTO;
import com.mindsync.mindsync.dto.ChatRoomResponseDTO;
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
    public ResponseEntity<ChatRoomResponseDTO> createRoom(@RequestBody ChatRoomRequestDTO requestDto) {
        ChatRoom chatRoom = chatRoomService.createRoom(
                requestDto.getTitle(),
                requestDto.getHost(),
                requestDto.getParticipants(),
                requestDto.getContent()
        );

        // 응답을 DTO로 변환하여 반환
        ChatRoomResponseDTO responseDto = ChatRoomResponseDTO.builder()
                .roomId(chatRoom.getRoomId())
                .title(chatRoom.getTitle())
                .host(chatRoom.getHost())
                .participants(chatRoom.getParticipants())
                .content(chatRoom.getContent())
                .agenda(chatRoom.getAgenda())
                .build();

        return ResponseEntity.ok(responseDto);
    }

}
