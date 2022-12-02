package com.example.EPPOI.repository;

import com.example.EPPOI.model.ThirdPartyRegistrationRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdRequestRegistrationRepository extends Neo4jRepository<ThirdPartyRegistrationRequest,Long> {
}
