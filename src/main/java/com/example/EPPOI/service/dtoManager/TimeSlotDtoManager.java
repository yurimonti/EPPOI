package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.PoiTagRelDTO;
import com.example.EPPOI.dto.TimeSlotDTO;
import com.example.EPPOI.model.TimeSlotNode;
import com.example.EPPOI.repository.TimeSlotRepository;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TimeSlotDtoManager implements DtoEntityManager<TimeSlotNode, TimeSlotDTO> {
    private final TimeSlotRepository timeSlotRepository;

    private void fillTimeSlot(TimeSlotNode entity, TimeSlotDTO dto) {
        List<String> monday = dto.getMonday();
        List<String> tuesday = dto.getTuesday();
        List<String> wednesday = dto.getWednesday();
        List<String> thursday = dto.getThursday();
        List<String> friday = dto.getFriday();
        List<String> saturday = dto.getSaturday();
        List<String> sunday = dto.getSunday();
        if (!monday.isEmpty()) monday.forEach(s -> entity.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> entity.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> entity.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> entity.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> entity.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> entity.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> entity.getSunday().add((LocalTime.parse(s))));
        this.timeSlotRepository.save(entity);
    }

    @Override
    public TimeSlotNode getEntityfromDto(TimeSlotDTO dto) throws NullPointerException {
        TimeSlotNode result;
        if(Objects.isNull(dto.getId())){
            result = new TimeSlotNode();
            this.fillTimeSlot(result,dto);
        } else result = this.timeSlotRepository.findById(dto.getId())
                .orElseThrow(() -> new NullPointerException("Time not found"));
        return result;
    }

    @Override
    public TimeSlotDTO getDtofromEntity(TimeSlotNode entity) throws NullPointerException {
        if(Objects.isNull(entity)) throw new NullPointerException("Time entity is null");
        return new TimeSlotDTO(entity);
    }
}
