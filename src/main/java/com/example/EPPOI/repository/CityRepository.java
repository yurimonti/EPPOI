package com.example.EPPOI.repository;

import com.example.EPPOI.model.CityNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends Neo4jRepository<CityNode,Long> {
    @Query("MATCH (n : CityNode) -- (m:PoiNode) WHERE ID(m)=$poiId \n" +
            "OPTIONAL MATCH (n)-[:CITY_CONTAINS_POI]-(p:PoiNode)\n" +
            "OPTIONAL MATCH (n)-[:CITY_HAS_COORDS]-(c:CoordsNode)\n" +
            "OPTIONAL MATCH (n)-[:CITY_PROVIDES_ITINERARY]-(i:ItineraryNode)\n" +
            "RETURN n as city,collect(p) as POIs,c as coords,collect(i) as itineraries" )
    <T extends CityNode> Optional<T> getCityByPoiId(Long poiId);

    @Query("MATCH (n : CityNode) -[r : CITY_PROVIDES_ITINERARY]- (m: ItineraryNode) WHERE ID(m)=$itineraryId RETURN n" )
    List<CityNode> getCityByItineraryId(Long itineraryId);
    CityNode findCityNodeByName(String name);
}
