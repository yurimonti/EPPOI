package com.example.EPPOI.security;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TokenCyclerRepository extends Neo4jRepository<RefreshTokenCycler,Long> {
}
