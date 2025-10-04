package com.mindsync.mindsync.repository;

import com.mindsync.mindsync.document.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {
}