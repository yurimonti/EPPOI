package com.example.EPPOI.repository;

import com.example.EPPOI.model.TagNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends Neo4jRepository<TagNode,Long> {
}
