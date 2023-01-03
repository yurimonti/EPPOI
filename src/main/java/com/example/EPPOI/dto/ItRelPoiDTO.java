package com.example.EPPOI.dto;

import com.example.EPPOI.model.ItineraryRelPoi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
