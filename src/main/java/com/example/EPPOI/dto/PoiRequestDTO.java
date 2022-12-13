package com.example.EPPOI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoiRequestDTO {
    private Long id;
    private StatusEnum status;
    private String name;
    private String description;
    private CityDTO city;
    private CoordsDTO coordinate;
    private TimeSlotDTO hours;
    private Double timeToVisit;
    private AddressDTO address;
    private Double ticketPrice;
    private String username;
    private PoiDTO poi;
    private List<PoiTypeDTO> types;
    private ContactDTO contact;
    private List<PoiTagRelDTO> tagValues;
}
