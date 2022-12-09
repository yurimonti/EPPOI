package com.example.EPPOI.dto;

import com.example.EPPOI.model.PoiTagRel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PoiTagRelDTO {
    private Long id;
    private TagDTO tag;
    private Boolean booleanValue;
    private String stringValue;

    public PoiTagRelDTO() {
        this.id = null;
        this.tag = null;
        this.booleanValue = null;
        this.stringValue = null;
    }

    public PoiTagRelDTO(TagDTO tag, Boolean booleanValue, String stringValue) {
        this();
        this.tag = tag;
        this.booleanValue = booleanValue;
        this.stringValue = stringValue;
    }

    public PoiTagRelDTO(PoiTagRel poiTagRel){
        this.id = poiTagRel.getId();
        this.tag=new TagDTO(poiTagRel.getTag());
        this.booleanValue = poiTagRel.getBooleanValue();
        this.stringValue = poiTagRel.getStringValue();
    }
}
