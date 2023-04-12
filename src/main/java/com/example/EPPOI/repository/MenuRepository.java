package com.example.EPPOI.repository;

import com.example.EPPOI.model.ThirdsElements.Menu;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface MenuRepository extends Neo4jRepository<Menu,Long> {
}
