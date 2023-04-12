package com.example.EPPOI.repository;

import com.example.EPPOI.model.ThirdPartyPoiRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyPoiRequestRepository extends Neo4jRepository<ThirdPartyPoiRequest,Long> {
}
