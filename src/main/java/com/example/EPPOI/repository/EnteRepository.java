package com.example.EPPOI.repository;

import com.example.EPPOI.model.user.EnteNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnteRepository extends Neo4jRepository<EnteNode,String> {
    List<EnteNode> findAllByCity_Name(String cityName);
}
