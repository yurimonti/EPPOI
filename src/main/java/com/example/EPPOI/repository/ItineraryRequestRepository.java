package com.example.EPPOI.repository;

import com.example.EPPOI.model.ItineraryRequestNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryRequestRepository extends Neo4jRepository<ItineraryRequestNode,Long> {
}
