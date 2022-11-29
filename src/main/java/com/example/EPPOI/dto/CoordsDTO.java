package com.example.EPPOI.dto;

import com.example.EPPOI.model.CoordsNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoordsDTO {
    private Long id;
    private Double lat;
    private Double lon;
    public CoordsDTO(Double lat, Double lon) {
        this();
        this.lat = lat;
        this.lon = lon;
    }
    public CoordsDTO(CoordsNode from) {
        this.id = from.getId();
        this.lat = from.getLat();
        this.lon = from.getLon();
    }

}
