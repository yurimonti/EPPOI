package com.example.EPPOI.repository;

import com.example.EPPOI.model.CoordsNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordsRepository extends Neo4jRepository<CoordsNode,Long> {
}
