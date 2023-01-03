package com.example.EPPOI.dto;

import com.example.EPPOI.model.CategoryNode;
import com.example.EPPOI.model.ItineraryRelPoi;
import com.example.EPPOI.model.ItineraryRequestNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryRequestDTO {
    private Long id;
    private String name;
    private String description;
    private List<ItRelPoiDTO> points;
    private List<CategoryDTO> categories;

    private List<CityDTO> cities;
    private Boolean accepted;
    private Integer consensus;
    private String createdBy;
    private Double timeToVisit;
    private List<String> geoJsonList;

    public ItineraryRequestDTO(ItineraryRequestNode from) {
        this.id = from.getId();
        this.name = from.getName();
        this.description = from.getDescription();
        this.createdBy = from.getCreatedBy();
        this.consensus = from.getConsensus();
        this.accepted = from.getAccepted();
        this.points = from.getPoints().stream().map(ItRelPoiDTO::new).toList();
        this.timeToVisit = from.getTimeToVisit();
        this.categories = from.getCategories().stream().map(CategoryDTO::new).toList();
        this.geoJsonList = from.getGeoJsonList();
    }
}
