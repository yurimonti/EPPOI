package com.example.EPPOI.repository;

import com.example.EPPOI.model.CityNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends Neo4jRepository<CityNode,Long> {
    CityNode findCityNodeByName(String name);
}
