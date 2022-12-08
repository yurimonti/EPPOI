package com.example.EPPOI.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryForm {
    private String name;
    private String description;
    //TODO: change to DTO
    private List<Long> POIsId;
}
