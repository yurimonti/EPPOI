package com.example.EPPOI.dto;

import com.example.EPPOI.model.ItineraryNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryDTO {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private Boolean isDefault;
    private List<ItRelPoiDTO> points;
    private Double timeToVisit;
    private List<String> geoJsonList;


    public ItineraryDTO(ItineraryNode from) {
        this.id = from.getId();
        this.name = from.getName();
        this.description = from.getDescription();
        this.createdBy = from.getCreatedBy();
        this.isDefault = from.getIsDefault();
        this.points = from.getPoints().stream().map(ItRelPoiDTO::new).toList();
        this.timeToVisit = from.getTimeToVisit();
        this.geoJsonList = from.getGeoJsonList();
    }
}
