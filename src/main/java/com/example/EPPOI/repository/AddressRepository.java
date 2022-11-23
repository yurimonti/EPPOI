package com.example.EPPOI.repository;

import com.example.EPPOI.model.AddressNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends Neo4jRepository<AddressNode,Long> {
}
