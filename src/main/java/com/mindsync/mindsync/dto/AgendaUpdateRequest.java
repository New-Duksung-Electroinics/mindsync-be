package com.mindsync.mindsync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AgendaUpdateRequest {
    private Map<String, String> data; // 클라이언트 요청 형태
}
