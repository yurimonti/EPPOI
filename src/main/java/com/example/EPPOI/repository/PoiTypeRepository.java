package com.example.EPPOI.repository;

import com.example.EPPOI.model.PoiTypeNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiTypeRepository extends Neo4jRepository<PoiTypeNode,Long> {
}
