package com.mindsync.mindsync.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaItem {
    private String title; // 목차 내용
    private String status; // 목차 진행 상태
}
