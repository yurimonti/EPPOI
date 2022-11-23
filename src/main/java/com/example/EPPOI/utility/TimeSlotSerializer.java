package com.example.EPPOI.utility;

import com.example.EPPOI.model.TimeSlotNode;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class TimeSlotSerializer implements  SerializerObject<TimeSlotNode> {
    @Override
    public TimeSlotNode deserializer(Map<String, Object> toDeserializer) {
        TimeSlotNode toFill = new TimeSlotNode();
        List<String> monday = (List<String>) toDeserializer.get("monday");
        List<String> tuesday = (List<String>) toDeserializer.get("tuesday");
        List<String> wednesday = (List<String>) toDeserializer.get("wednesday");
        List<String> thursday = (List<String>) toDeserializer.get("thursday");
        List<String> friday = (List<String>) toDeserializer.get("friday");
        List<String> saturday = (List<String>) toDeserializer.get("saturday");
        List<String> sunday = (List<String>) toDeserializer.get("sunday");
        if (!monday.isEmpty()) monday.forEach(s -> toFill.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> toFill.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> toFill.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> toFill.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> toFill.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> toFill.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> toFill.getSunday().add((LocalTime.parse(s))));
        return toFill;
    }
}
