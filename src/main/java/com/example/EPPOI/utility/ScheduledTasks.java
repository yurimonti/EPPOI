package com.example.EPPOI.utility;

import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.PoiRepository;
import com.example.EPPOI.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final TimeSlotRepository timeSlotRepository;
    private final PoiRepository poiRepository;

    private void updateOpenPoi(PoiNode poi, Calendar calendar){
        if(poi.getHours()!=null) {
            List<LocalTime> day = switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY -> poi.getHours().getMonday();
                case Calendar.TUESDAY -> poi.getHours().getTuesday();
                case Calendar.WEDNESDAY -> poi.getHours().getWednesday();
                case Calendar.THURSDAY -> poi.getHours().getThursday();
                case Calendar.FRIDAY -> poi.getHours().getFriday();
                case Calendar.SATURDAY -> poi.getHours().getSaturday();
                case Calendar.SUNDAY -> poi.getHours().getSunday();
                default -> new ArrayList<>();
            };
            boolean toSet = false;
            Instant instant = calendar.toInstant();
            ZoneId zoneId = TimeZone.getDefault().toZoneId();
            LocalTime toCompare = LocalTime.ofInstant(instant, zoneId);
            int l = day.size();
            if (l == 1) toSet = true;
            else if (l > 2) {
                if ((day.stream().toList().get(0).isBefore(toCompare) && day.stream().toList().get(1).isAfter(toCompare)) ||
                        (day.stream().toList().get(2).isBefore(toCompare) &&
                                day.stream().toList().get(3).isAfter(toCompare))) toSet = true;
            } else if (l == 2) {
                if (day.stream().toList().get(0).isBefore(toCompare) && day.stream().toList().get(1).isAfter(toCompare))
                    toSet = true;
            }
            poi.getHours().setIsOpen(toSet);
            this.timeSlotRepository.save(poi.getHours());
            this.poiRepository.save(poi);
        }
    }

    /**
     * Check and update if all pois are open in a certain date
     * @param date to validating the check
     */
    private void updateOpenPois(Date date){
        List<PoiNode> pois = this.poiRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        pois.forEach(pointOfInterestNode -> this.updateOpenPoi(pointOfInterestNode,calendar));
    }


    @Scheduled(fixedRate = 60000,initialDelay = 15000)
    @Async
    public void printScemo() {
        this.updateOpenPois(new Date());
        log.info("pois time open updated!!");
    }
}