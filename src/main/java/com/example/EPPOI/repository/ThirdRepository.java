package com.example.EPPOI.repository;

import com.example.EPPOI.model.user.ThirdUserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdRepository extends Neo4jRepository<ThirdUserNode,String> {

    ThirdUserNode findByUsername(String username);
}
