package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ChatMessageDto;
import com.mindsync.mindsync.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatMessageController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/{roomId}/enter")
    public void enterChatRoom(@Payload ChatMessageDto message, @DestinationVariable String roomId) {
        System.out.println("Entering room: " + roomId + " User: " + message.getEmail());
        message.setMessage(message.getEmail() + " is entering");
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
        System.out.println("message success: " + message.getMessage());
    }

    // 채팅 메시지 전송 처리
    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(@Payload ChatMessageDto message, @DestinationVariable String roomId) {
        System.out.println("Message received from " + message.getName() + ": " + message.getMessage());
        chatMessageService.saveMessage(roomId, message.getName(), message.getEmail(), message.getMessage(), message.getAgendaId());
        // 저장된 메시지를 모든 구독자에게 전송
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
        System.out.println("Message saved & sent to room: " + roomId);
    }
}
