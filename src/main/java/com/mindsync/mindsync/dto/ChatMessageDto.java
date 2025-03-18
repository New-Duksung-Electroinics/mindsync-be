package com.mindsync.mindsync.dto;

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
    private int agendaId;
    private String timestamp;
}
