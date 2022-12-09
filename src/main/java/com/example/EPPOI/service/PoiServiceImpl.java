package com.example.EPPOI.service;

import com.example.EPPOI.dto.AddressDTO;
import com.example.EPPOI.dto.ContactDTO;
import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoiServiceImpl implements PoiService {
    private final PoiRepository poiRepository;
    private final CityService cityService;
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final CoordsRepository coordsRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final TagRepository tagRepository;

    @Override
    public PoiNode findPoiById(Long id) {
        return this.poiRepository.findById(id).orElseThrow(() -> new NullPointerException("PoiNode not found"));
    }

    private void emptyTimeSlot(TimeSlotNode toEmpty) {
        log.info("0");
        log.info("timeslot : {}",toEmpty);
        toEmpty.setMonday(new ArrayList<>());
        log.info("1");
        toEmpty.setTuesday(new ArrayList<>());
        log.info("2");
        toEmpty.setWednesday(new ArrayList<>());
        log.info("3");
        toEmpty.setThursday(new ArrayList<>());
        log.info("4");
        toEmpty.setFriday(new ArrayList<>());
        log.info("5");
        toEmpty.setSaturday(new ArrayList<>());
        log.info("6");
        toEmpty.setSunday(new ArrayList<>());
        log.info("7");
        toEmpty.setIsOpen(false);
        log.info("8");
        this.timeSlotRepository.save(toEmpty);
        log.info("9");
    }

    private void fillTimeSlot(TimeSlotNode toFill, PoiForm form) {
        List<String> monday = form.getTimeSlot().getMonday();
        List<String> tuesday = form.getTimeSlot().getTuesday();
        List<String> wednesday = form.getTimeSlot().getWednesday();
        List<String> thursday = form.getTimeSlot().getThursday();
        List<String> friday = form.getTimeSlot().getFriday();
        List<String> saturday = form.getTimeSlot().getSaturday();
        List<String> sunday = form.getTimeSlot().getSunday();
        if (!monday.isEmpty()) monday.forEach(s -> toFill.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> toFill.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> toFill.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> toFill.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> toFill.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> toFill.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> toFill.getSunday().add((LocalTime.parse(s))));
        this.timeSlotRepository.save(toFill);
    }

    @Override
    public PoiNode setParamsToPoi(PoiNode target, PoiForm toSet) {
        target.setName(toSet.getName());
        target.setDescription(toSet.getDescription());
        CoordsDTO coords = toSet.getCoordinate();
        log.info("coordsDTO :{}", coords);
        target.getCoordinate().setLat(coords.getLat());
        target.getCoordinate().setLon(coords.getLon());
        this.coordsRepository.save(target.getCoordinate());
        log.info("coords :{}", target.getCoordinate());
        ContactDTO contacts = toSet.getContact();
        log.info("contactsDTO :{}", contacts);
        if (Objects.isNull(contacts)) {
            if (!Objects.isNull(target.getContact())) {
                this.contactRepository.delete(target.getContact());
                target.setContact(new ContactNode());
                this.contactRepository.save(target.getContact());
            }
        } else {
            target.getContact().setEmail(contacts.getEmail());
            target.getContact().setCellNumber(contacts.getCellNumber());
            target.getContact().setFax(contacts.getFax());
            this.contactRepository.save(target.getContact());
            log.info("contacts :{}", target.getContact());
        }
        AddressDTO address = toSet.getAddress();
        log.info("addressDTO :{}", address);
        if (Objects.isNull(address)) {
            if (!Objects.isNull(target.getAddress())) {
                this.addressRepository.delete(target.getAddress());
            }
        } else {
            target.getAddress().setStreet(address.getStreet());
            target.getAddress().setNumber(address.getNumber());
            this.addressRepository.save(target.getAddress());
            log.info("address :{}", target.getAddress());
        }
        target.setTimeToVisit(toSet.getTimeToVisit());
        log.info("timeToVisit :{}", target.getTimeToVisit());
        target.setTicketPrice(toSet.getTicketPrice());
        log.info("ticketPrice :{}", target.getTicketPrice());
        //FIXME:ERROR HERE
        this.emptyTimeSlot(target.getHours());
        log.info("empty hours :{}", target.getHours());
        this.fillTimeSlot(target.getHours(), toSet);
        log.info("filled hours :{}", target.getHours());
        target.setTypes(new ArrayList<>());
        log.info("empty types :{}", target.getTypes());
        toSet.getTypes().forEach(t -> target.getTypes().add(this.poiTypeRepository.findById(t.getId())
                .orElseThrow(() -> new NullPointerException("This type not exists"))));
        log.info("filled types :{}", target.getTypes());
        target.setTagValues(new ArrayList<>());
        log.info("empty tags :{}", target.getTagValues());
        if (!Objects.isNull(toSet.getTagValues())) {
            toSet.getTagValues().forEach(t -> {
                PoiTagRel rel = new PoiTagRel(this.tagRepository.findById(t.getTag().getId())
                        .orElseThrow(() -> new NullPointerException("This type not exists")));
                if (t.getTag().getIsBooleanType()) rel.setBooleanValue(t.getBooleanValue());
                else rel.setStringValue(t.getStringValue());
                target.getTagValues().add(rel);
            });
        }
        log.info("filled tags :{}", target.getTagValues());
        this.poiRepository.save(target);
        log.info("filled target :{}", target);
        return target;
    }

    @Override
    public void setCityToPoi(PoiNode poi, CityNode city) {
        city.getPOIs().add(poi);
        this.cityService.saveCity(city);
    }

    @Override
    public PoiNode savePoi(PoiNode toSave) {
        return this.poiRepository.save(toSave);
    }

    @Override
    public List<PoiNode> getAllPois() {
        return this.poiRepository.findAll();
    }
}
