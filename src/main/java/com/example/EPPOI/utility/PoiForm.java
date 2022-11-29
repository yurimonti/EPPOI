package com.example.EPPOI.utility;

import com.example.EPPOI.dto.CoordsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PoiForm{
    private String name;
    private String description;
    private CoordsDTO coordinate;
    private Double timeToVisit;
    private Double ticketPrice;
}
