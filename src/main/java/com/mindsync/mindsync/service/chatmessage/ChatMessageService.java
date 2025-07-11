package com.mindsync.mindsync.service.chatmessage;


import com.mindsync.mindsync.dto.chatroom.ChatRoomPastMessageDto;
import com.mindsync.mindsync.entity.ChatRoomMessages;
import java.util.*;

public interface ChatMessageService {

    void saveMessage(String roomId, String name, String email, String message, String agendaId);

    Optional<ChatRoomMessages> getMessagesByRoomId(String roomId);

    List<ChatRoomPastMessageDto> getAllMessages(String roomId);


}
