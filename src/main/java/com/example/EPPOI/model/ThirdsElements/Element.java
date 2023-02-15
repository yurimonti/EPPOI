package com.example.EPPOI.model.ThirdsElements;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Element {
    private String name;
    private Double price;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isFrozen;

    //modify with various bool for the allergic food
    private boolean containAllergicStuff;

}
