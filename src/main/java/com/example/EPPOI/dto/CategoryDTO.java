package com.example.EPPOI.dto;

import com.example.EPPOI.model.CategoryNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO(CategoryNode categoryNode) {
        this.id = categoryNode.getId();
        this.name = categoryNode.getName();
    }
}
