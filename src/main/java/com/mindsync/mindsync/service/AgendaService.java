package com.mindsync.mindsync.service;

import com.mindsync.mindsync.dto.chatroom.AgendaItem;
import com.mindsync.mindsync.entity.Agenda;
import com.mindsync.mindsync.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;

    public void updateAgenda(String roomId, Map<String, String> rawData) {
        Map<String, AgendaItem> agendas = new HashMap<>();
        rawData.forEach((key, title) -> {
            agendas.put(key, new AgendaItem(title, "pending"));
        });

        Agenda agenda = new Agenda(roomId, agendas);
        agendaRepository.save(agenda);
    }
}
