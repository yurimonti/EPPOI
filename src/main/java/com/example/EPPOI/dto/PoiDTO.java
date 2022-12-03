package com.example.EPPOI.dto;

import lombok.Data;

import java.util.List;

@Data
public class PoiDTO {
    private Long id;
    private String name;
    private String description;
    private CoordsDTO coordinate;
    private TimeSlotDTO hours;
    private Double timeToVisit;
    private AddressDTO address;
    private Double ticketPrice;
    private List<String> contributors;
    private List<PoiTypeDTO> types;
    private ContactDTO contact;
    private List<PoiTagRelDTO> tagValues;
}
