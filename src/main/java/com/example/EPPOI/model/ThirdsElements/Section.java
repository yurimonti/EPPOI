package com.example.EPPOI.model.ThirdsElements;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Section {
    private List<Element> elements;
}
