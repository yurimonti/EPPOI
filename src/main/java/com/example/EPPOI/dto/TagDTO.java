package com.example.EPPOI.dto;

import com.example.EPPOI.model.TagNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private Boolean isBooleanType;

    public TagDTO(String name, boolean isBooleanType) {
        this();
        this.name = name;
        this.isBooleanType = isBooleanType;
    }

    public TagDTO(TagNode tagNode){
        this();
        this.id = tagNode.getId();
        this.name = tagNode.getName();
        this.isBooleanType = tagNode.getIsBooleanType();
    }
}
