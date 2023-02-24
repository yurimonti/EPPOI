package com.example.EPPOI.service;

import com.example.EPPOI.dto.*;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.service.dtoManager.DtoEntityManager;
import com.example.EPPOI.utility.AbstractFactoryPoi;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoiServiceImpl implements PoiService {
    private final AbstractFactoryPoi abstractFactoryPoi;
    private final PoiRepository poiRepository;
    private final CityService cityService;
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final CoordsRepository coordsRepository;
    private final TimeSlotRepository timeSlotRepository;

    private final ItineraryService itineraryService;
    private final CategoryRepository categoryRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final TagRepository tagRepository;
    private final DtoEntityManager<PoiTagRel, PoiTagRelDTO> poiTagRelDTOManager;
    private final DtoEntityManager<CoordsNode, CoordsDTO> coordsDtoManager;
    private final DtoEntityManager<PoiTypeNode, PoiTypeDTO> typeDtoManager;
    private final DtoEntityManager<AddressNode, AddressDTO> addressDtoManager;
    private final DtoEntityManager<ContactNode, ContactDTO> contactDtoManager;
    private final DtoEntityManager<TimeSlotNode, TimeSlotDTO> timeDtoManager;

    @Override
    public PoiNode findPoiById(Long id) {
        return this.poiRepository.findById(id).orElseThrow(() -> new NullPointerException("PoiNode not found"));
    }

/*
    private void deleteTimeSlot(TimeSlotNode toDelete) {
        this.timeSlotRepository.delete(toDelete);
    }
    private void deleteContact(ContactNode toDelete) {
        this.contactRepository.delete(toDelete);
    }
    private void deleteAddress(AddressNode toDelete) {
        this.addressRepository.delete(toDelete);
    }
    private void deleteCoordinate(CoordsNode toDelete) {
        this.coordsRepository.delete(toDelete);
    }
*/

    private void deletePoiPrivate(PoiNode toDelete)throws NullPointerException{
        if(Objects.isNull(toDelete)) throw new NullPointerException("poi not available");
        this.timeSlotRepository.deleteById(toDelete.getHours().getId());
        this.coordsRepository.deleteById(toDelete.getCoordinate().getId());
        this.addressRepository.deleteById(toDelete.getAddress().getId());
        this.contactRepository.deleteById(toDelete.getContact().getId());
        toDelete.setTypes(new ArrayList<>());
        toDelete.setTagValues(new ArrayList<>());
        this.poiRepository.delete(toDelete);
    }

    @Override
    public void deletePoi(PoiNode toDelete) throws NullPointerException,IllegalArgumentException{
        if(Objects.isNull(toDelete)) throw new NullPointerException("poi not available");
        List<ItineraryNode> itineraries = this.itineraryService.getItinerariesFromPoi(toDelete);
        if(itineraries.stream().anyMatch(ItineraryNode::getIsDefault)) throw
                new IllegalArgumentException("Impossible to delete cause there are some itineraries linked to this poi");
        List<CityNode> citiesToSave = new ArrayList<>();
        itineraries.forEach(i -> citiesToSave.addAll(this.cityService.getCitiesByItinerary(i)));
        List<CityNode> distinctCities = citiesToSave.stream().distinct().toList();
        this.itineraryService.deleteItinerary(itineraries);
        this.deletePoiPrivate(toDelete);
        this.cityService.saveCities(distinctCities);
    }

    @Override
    public PoiNode createPoiFromParams(PoiForm form) {
        List<PoiTypeNode> types = form.getTypes().stream().map(this.typeDtoManager::getEntityfromDto).toList();
        log.info("types : {}",types);
        List<CategoryNode> categories = new ArrayList<>();
        types.forEach(t -> categories.addAll(t.getCategories()));
        List<Long> categoriesId = categories.stream().map(CategoryNode::getId).distinct().toList();
        log.info("categories : {}",categories.stream().distinct().toList());
        List<PoiTagRel> tagValues = form.getTagValues().stream().map(this.poiTagRelDTOManager::getEntityfromDto).toList();
        log.info("tag values : {}",tagValues);
        CategoryNode ristorazione = this.categoryRepository.findByName("Ristorazione");
        log.info("ristorazione : {}",ristorazione);
        CategoryNode mobility = this.categoryRepository.findByName("Mobilità");
        log.info("mobilità : {}",mobility);
        if (!Objects.isNull(ristorazione) && categoriesId.contains(ristorazione.getId()))
            return this.abstractFactoryPoi.createRestaurantPoi(form.getName(), form.getDescription(),
                    this.coordsDtoManager.getEntityfromDto(form.getCoordinate()),
                    this.timeDtoManager.getEntityfromDto(form.getTimeSlot()),
                    form.getTimeToVisit(), this.addressDtoManager.getEntityfromDto(form.getAddress()),
                    form.getTicketPrice(),types, this.contactDtoManager.getEntityfromDto(form.getContact()),
                    tagValues);
        if (!Objects.isNull(mobility) && categoriesId.contains(mobility.getId()))
            return this.abstractFactoryPoi.createEvPoi(form.getName(), form.getDescription(),
                    this.coordsDtoManager.getEntityfromDto(form.getCoordinate()),
                    this.timeDtoManager.getEntityfromDto(form.getTimeSlot()),
                    form.getTimeToVisit(), this.addressDtoManager.getEntityfromDto(form.getAddress()), form.getTicketPrice(),
                    types, this.contactDtoManager.getEntityfromDto(form.getContact()),tagValues);
        return this.abstractFactoryPoi.createBasePoi(form.getName(), form.getDescription(),
                this.coordsDtoManager.getEntityfromDto(form.getCoordinate()),
                this.timeDtoManager.getEntityfromDto(form.getTimeSlot()),
                form.getTimeToVisit(), this.addressDtoManager.getEntityfromDto(form.getAddress()), form.getTicketPrice(),
                types, this.contactDtoManager.getEntityfromDto(form.getContact()),tagValues);
    }

    private void emptyTimeSlot(TimeSlotNode toEmpty) {
        log.info("0");
        log.info("timeslot : {}", toEmpty);
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
        this.emptyTimeSlot(target.getHours());
        log.info("empty hours :{}", target.getHours());
        this.fillTimeSlot(target.getHours(), toSet);
        log.info("filled hours :{}", target.getHours());
        target.setTypes(new ArrayList<>());
        log.info("empty types :{}", target.getTypes());
        toSet.getTypes().forEach(t -> target.getTypes().add(this.typeDtoManager.getEntityfromDto(t)));
        log.info("filled types :{}", target.getTypes());
        target.setTagValues(new ArrayList<>());
        log.info("empty tags :{}", target.getTagValues());
        if (!Objects.isNull(toSet.getTagValues()))
            toSet.getTagValues().forEach(t -> target.getTagValues().add(this.poiTagRelDTOManager.getEntityfromDto(t)));
        log.info("filled tags :{}", target.getTagValues());
        this.poiRepository.save(target);
        log.info("filled target :{}", target);
        return target;
    }

    @Override
    public PoiNode poiFromRequest(RequestPoiNode toSet) {
        PoiNode result;
        result = Objects.isNull(toSet.getTarget()) ? new PoiNode() : toSet.getTarget();
        result.setName(toSet.getName());
        result.setDescription(toSet.getDescription());
        result.setTimeToVisit(toSet.getTimeToVisit());
        result.setTicketPrice(toSet.getTicketPrice());
        if(!result.getContributors().contains(toSet.getCreatedBy()))
            result.getContributors().add(toSet.getCreatedBy());
        log.info("settaggio base {} {} {} {}", result.getName(), result.getDescription(), result.getTimeToVisit()
                , result.getTicketPrice());
        result.setCoordinate(toSet.getCoordinate());
        List<PoiTagRel> values = new ArrayList<>();
        toSet.getTagValues().forEach(t -> values.add(new PoiTagRel(t.getTag(),t.getBooleanValue(),t.getStringValue())));
        result.setTagValues(values);
        log.info("settaggio tagValues {}", result.getTagValues());
        result.setTypes(new ArrayList<>());
        toSet.getTypes().forEach(t -> result.getTypes().add(t));
        log.info("settaggio types {}", result.getTypes());
        result.setAddress(toSet.getAddress());
        log.info("settaggio address {}", result.getAddress());
        result.setContact(toSet.getContact());
        log.info("settaggio contact {}", result.getContact());
        result.setHours(toSet.getHours());
        log.info("settaggio time {}", result.getHours());
        this.savePoi(result);
        log.info("poi created : {}", result);
        return result;
    }

    @Override
    public void setCityToPoi(PoiNode poi, CityNode city) {
        city.getPOIs().add(poi);
        log.info("adding poi: {} to city: {}", poi.getName(), city.getName());
        this.cityService.saveCity(city);
    }

    @Override
    public PoiDTO createDTOfromNode(PoiNode poi) {
        CityNode city = this.cityService.getCityByPoi(poi.getId());
        PoiDTO result = new PoiDTO(poi);
        result.setCity(new CityDTO(city));
        return result;
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
