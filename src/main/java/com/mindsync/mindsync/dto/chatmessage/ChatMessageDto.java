package com.mindsync.mindsync.dto.chatmessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    private String roomId;
    private String name;
    private String email;
    private String message;
    private String agendaId;
    private String timestamp;
}
