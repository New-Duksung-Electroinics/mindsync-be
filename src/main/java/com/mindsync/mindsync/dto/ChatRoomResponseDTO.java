package com.mindsync.mindsync.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatRoomResponseDTO {
    private String roomId;
    private String title;
    private String host;
    private List<String> participants;
    private String content;
    private List<String> agenda;
}
