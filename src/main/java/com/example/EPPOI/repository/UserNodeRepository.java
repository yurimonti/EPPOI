package com.example.EPPOI.repository;

import com.example.EPPOI.model.user.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {
    @Query("MATCH (n:UserNode) return n")
    List<UserNode> findAllUser();
    @Query("MATCH (n:UserNode {username: $username}) RETURN n")
    UserNode findByUsername(String username);
    //UserNode findByUsername(String username);
}
