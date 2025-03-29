package com.mindsync.mindsync.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomRequestDto {
    private String title; // 회의 제목
    private String host_email; // 회의 주최자 이메일
    private List<String> participants; // 참여 멤버 리스트
    private String content; // 회의 예상 내용
    private String mbti; // 헬퍼 mbti
}
