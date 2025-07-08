package com.mindsync.mindsync.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Summary {
    private String agendaId;
    private String topic;
    private String content;
}
