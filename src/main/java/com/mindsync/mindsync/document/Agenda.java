package com.mindsync.mindsync.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mindsync.mindsync.dto.response.AgendaItemResponse;

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
    private Map<String, AgendaItemResponse> agendas;

}
