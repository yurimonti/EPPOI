package com.example.EPPOI.repository;

import com.example.EPPOI.model.user.TouristNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends Neo4jRepository<TouristNode,String> {
    TouristNode findByUsername(String username);
}
