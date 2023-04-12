package com.example.EPPOI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Node
@Data
@AllArgsConstructor
public class ThirdPartyPoiRequest {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Boolean isAccepted;
    private CoordsNode coordinate;
    private TimeSlotNode hours;
    private AddressNode address;
    @Relationship(type = "POI_HAS_TYPE",direction = Relationship.Direction.OUTGOING)
    private List<PoiTypeNode> types;
    private ContactNode contact;
    @Relationship(type = "POI_TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private List<PoiTagRel> tagValues;

    public ThirdPartyPoiRequest() {
        this.isAccepted = null;
        this.types = new ArrayList<>();
        this.tagValues = new ArrayList<>();

    }

    public ThirdPartyPoiRequest(String name, String description, CoordsNode coordinate, TimeSlotNode hours,
                                AddressNode address, List<PoiTypeNode> types,
                                ContactNode contact, List<PoiTagRel> tagValues) {
        this();
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
        this.hours = hours;
        this.address = address;
        this.types = types;
        this.contact = contact;
        this.tagValues = tagValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThirdPartyPoiRequest request = (ThirdPartyPoiRequest) o;
        return getId().equals(request.getId()) && getName().equals(request.getName())
                && getDescription().equals(request.getDescription()) && Objects.equals(getIsAccepted(), request.getIsAccepted())
                && Objects.equals(getCoordinate(), request.getCoordinate()) && Objects.equals(getHours(), request.getHours())
                && Objects.equals(getAddress(), request.getAddress()) && getTypes().equals(request.getTypes())
                && Objects.equals(getContact(), request.getContact()) && getTagValues().equals(request.getTagValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getIsAccepted(), getCoordinate(), getHours(), getAddress(), getTypes(), getContact(), getTagValues());
    }
}
