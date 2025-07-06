package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.AgendaItem;
import com.mindsync.mindsync.dto.ChatRoomPastMessageDto;
import com.mindsync.mindsync.entity.ChatRoomMessages;
import com.mindsync.mindsync.entity.ChatRoomMessages.Message;
import com.mindsync.mindsync.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveMessage(String roomId, String name, String email, String message, String agendaId) {
        Optional<ChatRoomMessages> optionalChatRoom = chatMessageRepository.findById(roomId);

        ChatRoomMessages chatRoom;
        if (optionalChatRoom.isPresent()) {
            chatRoom = optionalChatRoom.get();
        } else {
            chatRoom = ChatRoomMessages.builder()
                    .roomId(roomId)
                    .messages(new HashMap<>())
                    .build();
        }

        List<Message> messageList = chatRoom.getMessages().getOrDefault(agendaId, new ArrayList<>());

        // 새로운 메시지 추가
        messageList.add(Message.builder()
                .name(name)
                .email(email)
                .message(message)
                .agendaId(agendaId)
                .timestamp(LocalDateTime.now())
                .build());

        chatRoom.getMessages().put(agendaId, messageList);
        chatMessageRepository.save(chatRoom);
    }

    public Optional<ChatRoomMessages> getMessagesByRoomId(String roomId) {
        return chatMessageRepository.findById(roomId);
    }

    public List<ChatRoomPastMessageDto> getAllMessages(String roomId) {
        Optional<ChatRoomMessages> optionalChatRoom = chatMessageRepository.findById(roomId);

        if (optionalChatRoom.isEmpty()) {
            return new ArrayList<>();
        }

        ChatRoomMessages chatRoom = optionalChatRoom.get();
        List<ChatRoomPastMessageDto> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        chatRoom.getMessages().forEach((agendaId, messages) -> {
            messages.forEach(msg -> {
                result.add(ChatRoomPastMessageDto.builder()
                        .name(msg.getName())
                        .email(msg.getEmail())
                        .message(msg.getMessage())
                        .agendaId(msg.getAgendaId())
                        .timestamp(msg.getTimestamp().format(formatter))
                        .build());
            });
        });

        result.sort(Comparator.comparing(ChatRoomPastMessageDto::getTimestamp));
        return result;
    }



}
