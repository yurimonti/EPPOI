package com.example.EPPOI.dto;

import com.example.EPPOI.model.ItineraryRelPoi;
import lombok.Data;

@Data
public class ItRelPoiDTO {
    private Long id;
    private PoiDTO poi;
    private Integer index;

    public ItRelPoiDTO(ItineraryRelPoi from) {
        this.id = from.getId();
        this.poi = new PoiDTO(from.getPoi());
        this.index = from.getIndex();
    }
}
