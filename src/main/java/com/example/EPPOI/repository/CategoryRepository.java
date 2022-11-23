package com.example.EPPOI.repository;

import com.example.EPPOI.model.CategoryNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends Neo4jRepository<CategoryNode,Long> {
    CategoryNode findByName(String name);
}
