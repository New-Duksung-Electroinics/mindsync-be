package com.mindsync.mindsync.service;


import com.mindsync.mindsync.dto.response.ChatRoomPastMessageResponse;
import com.mindsync.mindsync.document.ChatRoomMessages;
import com.mindsync.mindsync.document.ChatRoomMessages.Message;
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

        ChatRoomMessages chatRoom = optionalChatRoom.orElseGet(() ->
                ChatRoomMessages.builder()
                        .roomId(roomId)
                        .messages(new HashMap<>())
                        .build()
        );

        List<Message> messageList = chatRoom.getMessages().getOrDefault(agendaId, new ArrayList<>());

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

    public List<ChatRoomPastMessageResponse> getAllMessages(String roomId) {
        Optional<ChatRoomMessages> optional = chatMessageRepository.findById(roomId);
        if (optional.isEmpty()) return new ArrayList<>();

        ChatRoomMessages chatRoom = optional.get();
        List<ChatRoomPastMessageResponse> result = new ArrayList<>();

        // 1) Message를 시간으로 정렬한 뒤
        List<Message> flat = new ArrayList<>();
        chatRoom.getMessages().values().forEach(flat::addAll);
        flat.sort(Comparator.comparing(Message::getTimestamp)); // LocalDateTime 기준

        // 2) 마지막에 문자열 포맷 적용
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (Message msg : flat) {
            result.add(ChatRoomPastMessageResponse.builder()
                    .name(msg.getName())
                    .email(msg.getEmail())
                    .message(msg.getMessage())
                    .agendaId(msg.getAgendaId())
                    .timestamp(msg.getTimestamp().format(formatter))
                    .build());
        }
        return result;
    }
}



