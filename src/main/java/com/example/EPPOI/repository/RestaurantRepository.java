package com.example.EPPOI.repository;

import com.example.EPPOI.model.poi.RestaurantPoiNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends Neo4jRepository<RestaurantPoiNode,Long> {
}
