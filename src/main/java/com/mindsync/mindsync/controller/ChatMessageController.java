package com.mindsync.mindsync.controller;

import com.mindsync.mindsync.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{roomId}/enter")
    public void enterChatRoom(@Payload ChatMessageDTO message, @DestinationVariable String roomId) {
        System.out.println("enter send: " + message.getSender());
        message.setMessage(message.getSender() + "is enter the room");
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
        System.out.println("message success: " + message.getMessage());
    }

    // 채팅 메시지 전송 처리
    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(@Payload ChatMessageDTO message, @DestinationVariable String roomId) {
        System.out.println("Message received from " + message.getSender() + ": " + message.getMessage());
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
        System.out.println("Message sent to room: " + roomId);
    }
}
