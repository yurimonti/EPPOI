package com.example.EPPOI.dto;

import com.example.EPPOI.model.CityNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private Long id;
    private String name;

    public CityDTO(CityNode cityNode) {
        this.id = cityNode.getId();
        this.name = cityNode.getName();
    }

}
