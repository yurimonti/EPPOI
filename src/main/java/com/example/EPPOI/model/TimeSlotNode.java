package com.example.EPPOI.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Node
public class TimeSlotNode {
    @Id
    @GeneratedValue
    private Long id;

    private List<LocalTime> monday;
    private List<LocalTime> tuesday;
    private List<LocalTime> wednesday;
    private List<LocalTime> thursday;
    private List<LocalTime> friday;
    private List<LocalTime> saturday;
    private List<LocalTime> sunday;

    private Boolean isOpen;

    public TimeSlotNode() {
        this.monday = new ArrayList<>();
        this.tuesday = new ArrayList<>();
        this.wednesday = new ArrayList<>();
        this.thursday = new ArrayList<>();
        this.friday = new ArrayList<>();
        this.saturday = new ArrayList<>();
        this.sunday = new ArrayList<>();
        this.isOpen = false;
    }

    public TimeSlotNode(
            List<LocalTime> monday, List<LocalTime> tuesday, List<LocalTime> wednesday,
            List<LocalTime> thursday, List<LocalTime> friday, List<LocalTime> saturday,
            List<LocalTime> sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.isOpen = false;
    }
}
