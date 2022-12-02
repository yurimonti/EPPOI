package com.example.EPPOI.repository;

import com.example.EPPOI.model.poi.PoiNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiRepository extends Neo4jRepository<PoiNode,Long> {
}
