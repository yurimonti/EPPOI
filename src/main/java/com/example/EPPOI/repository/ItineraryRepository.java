package com.example.EPPOI.repository;

import com.example.EPPOI.model.ItineraryNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryRepository extends Neo4jRepository<ItineraryNode,Long> {
}
