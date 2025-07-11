package com.mindsync.mindsync.service.chatroom;

import com.mindsync.mindsync.entity.ChatRoom;
import com.mindsync.mindsync.repository.ChatRoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ChatRoomService {

    ChatRoom createRoom(String title, String host_email, List<String> participants, String content, String mbti);

    ChatRoom getSummaryByRoomId(String roomId);

}
