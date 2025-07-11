package com.mindsync.mindsync.service.chatroom;

import java.util.Map;

public interface AgendaService {

    void updateAgenda(String roomId, Map<String, String> rawData);
}
