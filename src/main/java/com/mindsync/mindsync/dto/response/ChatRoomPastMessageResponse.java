package com.mindsync.mindsync.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomPastMessageResponse {
    private String name;
    private String email;
    private String message;
    private String agendaId;
    private String timestamp;
}
