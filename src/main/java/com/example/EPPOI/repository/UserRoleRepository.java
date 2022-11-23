package com.example.EPPOI.repository;

import com.example.EPPOI.model.user.UserRoleNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends Neo4jRepository<UserRoleNode,Long> {
    UserRoleNode findByName(String name);
}
