package com.example.EPPOI.dto;

import com.example.EPPOI.model.CoordsNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoordsDTO {
    private Long id;
    private Double latitude;
    private Double longitude;
    public CoordsDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public CoordsDTO(CoordsNode from) {
        this(from.getId(), from.getLat(), from.getLon());
    }
}
