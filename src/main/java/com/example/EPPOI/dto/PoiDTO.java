package com.example.EPPOI.dto;

import com.example.EPPOI.model.ThirdsElements.Menu;
import com.example.EPPOI.model.poi.PoiNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoiDTO {
    private Long id;
    private String name;
    private String description;
    private CoordsDTO coordinate;
    private TimeSlotDTO hours;
    private Double timeToVisit;
    private AddressDTO address;
    private Double ticketPrice;

    private CityDTO city;
    private List<String> contributors;
    private List<PoiTypeDTO> types;
    private ContactDTO contact;
    private List<PoiTagRelDTO> tagValues;

    //TODO Elements for thirds
    private boolean isThird;

    private Menu menu;




    public PoiDTO(PoiNode poi){
        this.id = poi.getId();
        this.name = poi.getName();
        this.description = poi.getDescription();
        this.tagValues = poi.getTagValues().stream().map(PoiTagRelDTO::new).toList();
        this.coordinate = new CoordsDTO(poi.getCoordinate());
        this.contributors = poi.getContributors();
        this.types = poi.getTypes().stream().map(PoiTypeDTO::new).toList();
        this.contact = new ContactDTO(poi.getContact());
        this.ticketPrice = poi.getTicketPrice();
        this.timeToVisit = poi.getTimeToVisit();
        this.address = new AddressDTO(poi.getAddress());
        this.hours = new TimeSlotDTO(poi.getHours());
    }

}
