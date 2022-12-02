package com.example.EPPOI.dto;

import com.example.EPPOI.model.CategoryNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private String name;

    public CategoryDTO(CategoryNode categoryNode) {
        this.name = categoryNode.getName();
    }
}
