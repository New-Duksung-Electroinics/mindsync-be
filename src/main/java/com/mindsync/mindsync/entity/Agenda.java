package com.mindsync.mindsync.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mindsync.mindsync.dto.AgendaItem;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "agenda")
public class Agenda {
    @Id
    private String roomId;
    private Map<String, AgendaItem> agendas;

}
