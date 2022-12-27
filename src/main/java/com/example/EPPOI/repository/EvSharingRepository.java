package com.example.EPPOI.repository;

import com.example.EPPOI.model.poi.EVSharingNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvSharingRepository extends Neo4jRepository<EVSharingNode,Long> {
}
