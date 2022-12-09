package com.example.EPPOI.utility;

import com.example.EPPOI.dto.*;
import com.example.EPPOI.model.PoiTypeNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PoiForm{
    private Long idPoi;
    private String name;
    private String description;
    private CoordsDTO coordinate;
    private TimeSlotDTO timeSlot;
    private AddressDTO address;
    private Double timeToVisit;
    private Double ticketPrice;
    private List<PoiTypeDTO> types;
    private ContactDTO contact;
    private List<PoiTagRelDTO> tagValues;
}
