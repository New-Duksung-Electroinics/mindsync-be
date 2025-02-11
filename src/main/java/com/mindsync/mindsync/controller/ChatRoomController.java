package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ChatRoomRequestDTO;
import com.mindsync.mindsync.dto.ChatRoomResponseDTO;
import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.service.ChatRoomService;
import com.mindsync.mindsync.utils.ResponseUtil;
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
    public ResponseDto createRoom(@RequestBody ChatRoomRequestDTO requestDto) {
        try {
            ChatRoom chatRoom = chatRoomService.createRoom(
                    requestDto.getTitle(),
                    requestDto.getHost(),
                    requestDto.getParticipants(),
                    requestDto.getContent()
            );

            Map<String, String> responseData = Map.of(
                    "roomId", chatRoom.getRoomId()
            );


            return ResponseUtil.SUCCESS("방 생성이 완료되었습니다.", responseData);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.ERROR("서버 에러가 발생했습니다.", null)).getBody();
        }
        }
    }

