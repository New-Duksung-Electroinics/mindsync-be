package com.mindsync.mindsync.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomPastMessageDto {
    private String name;
    private String email;
    private String message;
    private String agendaId;
    private String timestamp;
}
