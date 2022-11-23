package com.example.EPPOI.repository;

import com.example.EPPOI.model.TimeSlotNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends Neo4jRepository<TimeSlotNode,Long> {
}
