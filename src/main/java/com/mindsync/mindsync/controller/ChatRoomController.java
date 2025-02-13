package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ChatRoomRequestDTO;
import com.mindsync.mindsync.dto.EmailSearchDto;
import com.mindsync.mindsync.dto.ResponseDto;
import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.service.ChatRoomService;
import com.mindsync.mindsync.service.UserService;
import com.mindsync.mindsync.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public ChatRoomController(ChatRoomService chatRoomService, UserService userService) {
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }

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

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<EmailSearchDto>>> searchUsers(@RequestParam String query) {
        List<EmailSearchDto> users = userService.searchUsersByEmail(query);

        if (users.isEmpty()) {
            return ResponseEntity.ok(ResponseUtil.SUCCESS("검색 결과가 없습니다.", Collections.emptyList()));
        }

        return ResponseEntity.ok(ResponseUtil.SUCCESS("검색 완료했습니다.", users));
    }
}

