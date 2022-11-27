package com.example.EPPOI.repository;

import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends Neo4jRepository<TouristNode,String> {

    @Query("MATCH (n:UserNode WHERE n.verificationCode = $code ) RETURN n")
    TouristNode findByVerificationCode(@Param("code") String code);
}
