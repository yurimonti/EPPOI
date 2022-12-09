package com.example.EPPOI.dto;

import com.example.EPPOI.model.TimeSlotNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TimeSlotDTO {
    private Long id;
    private List<String> monday;
    private List<String> tuesday;
    private List<String> wednesday;
    private List<String> thursday;
    private List<String> friday;
    private List<String> saturday;
    private List<String> sunday;
    private Boolean isOpen;

    public TimeSlotDTO(){
        this.monday = new ArrayList<>();
        this.tuesday = new ArrayList<>();
        this.wednesday = new ArrayList<>();
        this.thursday = new ArrayList<>();
        this.friday = new ArrayList<>();
        this.saturday = new ArrayList<>();
        this.sunday = new ArrayList<>();
        this.isOpen = false;
    }

    public TimeSlotDTO(List<String> monday, List<String> tuesday, List<String> wednesday, List<String> thursday,
                       List<String> friday, List<String> saturday, List<String> sunday, boolean isOpen) {
        this();
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.isOpen = isOpen;
    }

    public TimeSlotDTO(TimeSlotNode timeSlot){
        this();
        this.id = timeSlot.getId();
        timeSlot.getMonday().forEach(localTime -> this.monday.add(localTime.toString()));
        timeSlot.getTuesday().forEach(localTime -> this.tuesday.add(localTime.toString()));
        timeSlot.getWednesday().forEach(localTime -> this.wednesday.add(localTime.toString()));
        timeSlot.getThursday().forEach(localTime -> this.thursday.add(localTime.toString()));
        timeSlot.getFriday().forEach(localTime -> this.friday.add(localTime.toString()));
        timeSlot.getSaturday().forEach(localTime -> this.saturday.add(localTime.toString()));
        timeSlot.getSunday().forEach(localTime -> this.sunday.add(localTime.toString()));
        this.isOpen = timeSlot.getIsOpen();
    }
}
