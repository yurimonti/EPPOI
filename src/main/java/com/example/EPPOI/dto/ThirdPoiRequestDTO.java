package com.example.EPPOI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPoiRequestDTO {
    private Long id;
    private StatusEnum status;
    private String name;
    private String description;
    private CoordsDTO coordinate;
    private TimeSlotDTO hours;
    private AddressDTO address;
    private List<PoiTypeDTO> types;
    private ContactDTO contact;
    private List<PoiTagRelDTO> tagValues;
}

