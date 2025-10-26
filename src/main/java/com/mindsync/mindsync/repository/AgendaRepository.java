package com.mindsync.mindsync.repository;

import ch.qos.logback.core.joran.util.AggregationAssessor;
import com.mindsync.mindsync.document.Agenda;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {
    Optional<Agenda> findByRoomId(String roomId);
}