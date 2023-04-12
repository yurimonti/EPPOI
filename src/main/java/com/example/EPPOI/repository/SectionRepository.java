package com.example.EPPOI.repository;

import com.example.EPPOI.model.ThirdsElements.Section;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SectionRepository extends Neo4jRepository<Section,Long> {
}
