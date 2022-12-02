package com.example.EPPOI.dto;

import com.example.EPPOI.model.PoiTypeNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PoiTypeDTO {
    private String name;
    private List<CategoryDTO> categories;
    private List<TagDTO> tags;

    public PoiTypeDTO(String name, List<CategoryDTO> categories, List<TagDTO> tags) {
        this();
        this.name = name;
        this.categories = categories;
        this.tags = tags;
    }

    public PoiTypeDTO(PoiTypeNode poiType) {
        this();
        this.name = poiType.getName();
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.categories.addAll(poiType.getCategories().stream().map(CategoryDTO::new).toList());
        this.tags.addAll(poiType.getTags().stream().map(TagDTO::new).toList());
    }
}