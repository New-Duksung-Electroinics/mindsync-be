package com.mindsync.mindsync.controller;


import com.mindsync.mindsync.dto.*;
import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.jwt.JWTUtil;
import com.mindsync.mindsync.service.AgendaService;
import com.mindsync.mindsync.service.ChatMessageService;
import com.mindsync.mindsync.service.ChatRoomService;
import com.mindsync.mindsync.service.UserService;
import com.mindsync.mindsync.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;

    private final AgendaService agendaService;

    private final JWTUtil jwtUtil;


    private final SimpMessagingTemplate messagingTemplate;


    public ChatRoomController(ChatRoomService chatRoomService, UserService userService, AgendaService agendaService, SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, JWTUtil jwtUtil) {
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.agendaService = agendaService;
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/room")
    // 방 생성할 때, 호스트도 넣어줌!
    public ResponseDto createRoom(@RequestBody ChatRoomRequestDto requestDto) {
        try {
            ChatRoom chatRoom = chatRoomService.createRoom(
                    requestDto.getTitle(),
                    requestDto.getHost_email(),
                    requestDto.getParticipants(),
                    requestDto.getContent(),
                    requestDto.getMbti()
            );
            Map<String, String> responseData = Map.of(
                    "roomId", chatRoom.getRoomId()
            );
            return ResponseUtil.SUCCESS("방 생성이 완료되었습니다.", responseData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.ERROR("서버 에러가 발생했습니다.", null)).getBody();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<EmailSearchDto>>> searchUsers(@RequestParam String query) {
        List<EmailSearchDto> users = userService.searchUsersByEmail(query);

        if (users.isEmpty()) {
            return ResponseEntity.ok(ResponseUtil.SUCCESS("검색 결과가 없습니다.", Collections.emptyList()));
        }

        return ResponseEntity.ok(ResponseUtil.SUCCESS("검색 완료했습니다.", users));
    }

    @GetMapping("/messages")
    public ResponseDto<List<ChatRoomPastMessageDto>> getMessages(
            @RequestHeader("Authorization") String header,
            @RequestParam String roomId) {
        try {
            if (header == null || !header.startsWith("Bearer ")) {
                return ResponseUtil.ERROR("Authorization header 누락 또는 잘못됨", null);
            }

            String token = header.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseUtil.ERROR("유효하지 않은 AccessToken", null);
            }

            List<ChatRoomPastMessageDto> messages = chatMessageService.getAllMessages(roomId);
            if (messages.isEmpty()) {
                return ResponseUtil.SUCCESS("저장된 메시지가 없습니다.", messages);
            }

            return ResponseUtil.SUCCESS("저장된 메시지를 반환합니다.", messages);
        } catch (Exception e) {
            return ResponseUtil.ERROR("메시지 조회 중 서버 에러가 발생했습니다.", null);
        }
    }

    @PostMapping("/agenda/{roomId}")
    public ResponseEntity<?> updateAgenda(@PathVariable String roomId,
                                          @RequestBody AgendaUpdateRequest request) {

        agendaService.updateAgenda(roomId, request.getData());
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "목차 수정을 완료했습니다."
        ));
    }


}

