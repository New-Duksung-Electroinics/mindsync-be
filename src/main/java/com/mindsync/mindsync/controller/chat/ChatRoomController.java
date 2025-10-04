package com.mindsync.mindsync.controller.chat;

import com.mindsync.mindsync.config.jwt.JwtUtil;
import com.mindsync.mindsync.dto.request.AgendaUpdateRequest;
import com.mindsync.mindsync.dto.response.ChatRoomPastMessageResponse;
import com.mindsync.mindsync.dto.request.ChatRoomCreateRequest;
import com.mindsync.mindsync.dto.response.EmailSearchResponse;
import com.mindsync.mindsync.dto.response.CommonResponse;
import com.mindsync.mindsync.document.ChatRoom;
import com.mindsync.mindsync.service.AgendaService;
import com.mindsync.mindsync.service.ChatMessageService;
import com.mindsync.mindsync.service.ChatRoomService;
import com.mindsync.mindsync.service.UserService;
import com.mindsync.mindsync.utils.ResponseUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;

    private final AgendaService agendaService;

    private final JwtUtil jwtUtil;


    private final SimpMessagingTemplate messagingTemplate;


    public ChatRoomController(ChatRoomService chatRoomService, UserService userService, AgendaService agendaService, SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, JwtUtil jwtUtil) {
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.agendaService = agendaService;
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/room")
    // 방 생성할 때, 호스트도 넣어줌!
    public CommonResponse createRoom(@RequestBody ChatRoomCreateRequest requestDto) {
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
    public ResponseEntity<CommonResponse<List<EmailSearchResponse>>> searchUsers(@RequestParam String query) {
        List<EmailSearchResponse> users = userService.searchUsersByEmail(query);

        if (users.isEmpty()) {
            return ResponseEntity.ok(ResponseUtil.SUCCESS("검색 결과가 없습니다.", Collections.emptyList()));
        }

        return ResponseEntity.ok(ResponseUtil.SUCCESS("검색 완료했습니다.", users));
    }

    @GetMapping("/messages")
    public CommonResponse<List<ChatRoomPastMessageResponse>> getMessages(
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

            List<ChatRoomPastMessageResponse> messages = chatMessageService.getAllMessages(roomId);
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

    @GetMapping("/{roomId}/summary")
    public ResponseEntity<?> getChatRoomSummary(@PathVariable String roomId) {
        ChatRoom chatRoom = chatRoomService.getSummaryByRoomId(roomId);

        return ResponseEntity.ok(Map.of(
                "roomId", chatRoom.getRoomId(),
                "summary", chatRoom.getSummary()
        ));
    }


}