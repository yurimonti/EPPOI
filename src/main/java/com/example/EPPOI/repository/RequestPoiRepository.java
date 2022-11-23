package com.example.EPPOI.repository;

import com.example.EPPOI.model.RequestPoiNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPoiRepository extends Neo4jRepository<RequestPoiNode,Long> {
}
