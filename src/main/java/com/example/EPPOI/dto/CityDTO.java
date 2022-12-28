package com.example.EPPOI.dto;

import com.example.EPPOI.model.CityNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private Long id;

    private Long identifier;
    private String name;

    private CoordsDTO coordinates;
    private List<PoiDTO> pois;

    private List<ItineraryDTO> itineraries;

    public CityDTO(CityNode cityNode) {
        this.id = cityNode.getId();
        this.identifier = cityNode.getIdentifier();
        this.name = cityNode.getName();
        this.pois = cityNode.getPOIs().stream().map(PoiDTO::new).toList();
        this.itineraries = cityNode.getItineraries().stream().map(ItineraryDTO::new).toList();
        this.coordinates = new CoordsDTO(cityNode.getCoords());
    }

}
