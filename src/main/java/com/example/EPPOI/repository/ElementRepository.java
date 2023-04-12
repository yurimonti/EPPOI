package com.example.EPPOI.repository;

import com.example.EPPOI.model.ThirdsElements.Element;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ElementRepository extends Neo4jRepository<Element,Long> {
}
