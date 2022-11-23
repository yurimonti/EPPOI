package com.example.EPPOI.repository;

import com.example.EPPOI.model.ContactNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends Neo4jRepository<ContactNode,Long> {
}
