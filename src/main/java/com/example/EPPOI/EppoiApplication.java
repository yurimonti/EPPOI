package com.example.EPPOI;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalTime;
import java.util.*;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class EppoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EppoiApplication.class, args);
	}

	/*public void createPoiProva(CoordsRepository coordinateRepository, PoiRepository pointOfIntRepository,
							   String nome, CityNode city, PoiTypeNode type, CoordsNode coordinate,
							   Map<String, List<LocalTime>> orari, TimeSlotRepository timeSlotRepository,
							   ContactRepository contactRepository, AddressRepository addressRepository,CityRepository cityRepository){

		coordinateRepository.save(coordinate);
		ContactNode contact = new ContactNode("asdf@asdf.com","1234567890","nonSoComeSiScriveUnFax");
		contactRepository.save(contact);
		AddressNode address = new AddressNode("via Qualcosa",6);
		addressRepository.save(address);
		TimeSlotNode hours = new TimeSlotNode(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
				orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
		timeSlotRepository.save(hours);
		PoiNode pointProva = new PoiNode(nome,"descrizione prova 123",coordinate,hours,20.00,address,0.0,
				Arrays.asList(type),contact);
		for(TagNode tag : type.getTags()){
			PoiTagRel poiTagRel = new PoiTagRel(tag);
			if(tag.getIsBooleanType())poiTagRel.setBooleanValue(true);
			else poiTagRel.setStringValue("");
			pointProva.getTagValues().add(poiTagRel);
		}
		pointOfIntRepository.save(pointProva);
		city.getPOIs().add(pointProva);
		cityRepository.save(city);
	}

	@Bean
	CommandLineRunner initDatabase(TagRepository tagRepository, CityRepository cityRepository,
								   PoiRepository poiRepository, CoordsRepository coordsRepository,
								   ItineraryRepository itineraryRepository,UserRoleRepository userRoleRepository,
								   UserNodeRepository userNodeRepository,RequestPoiRepository requestPoiRepository,
								   ThirdRequestRegistrationRepository thirdRequestRegistrationRepository,
								   CategoryRepository categoryRepository,PoiTypeRepository poiTypeRepository,
								   ContactRepository contactRepository,AddressRepository addressRepository,
								   TimeSlotRepository timeSlotRepository,
								   ItineraryRequestRepository itineraryRequestRepository){
		return args -> {
			requestPoiRepository.deleteAll();
			userNodeRepository.deleteAll();
			userRoleRepository.deleteAll();
			itineraryRepository.deleteAll();
			tagRepository.deleteAll();
			cityRepository.deleteAll();
            poiRepository.deleteAll();
			coordsRepository.deleteAll();
			thirdRequestRegistrationRepository.deleteAll();
			addressRepository.deleteAll();
			categoryRepository.deleteAll();
			contactRepository.deleteAll();
			poiTypeRepository.deleteAll();
			timeSlotRepository.deleteAll();
			itineraryRepository.deleteAll();
			itineraryRequestRepository.deleteAll();
			CoordsNode camerinoCoords = new CoordsNode(43.139850, 13.069172);
			coordsRepository.save(camerinoCoords);
			CoordsNode castelCoords = new CoordsNode(43.209100, 13.054600);
			coordsRepository.save(castelCoords);
			//Creazione TagNode
			TagNode tag1 = new TagNode("ingresso animali",true);
			TagNode tag2 = new TagNode("accessibilita disabili",true);
			TagNode tag3 = new TagNode("potabile",true);
			//TagNode tag4 = new TagNode("costo",false);
			tagRepository.save(tag1);
			tagRepository.save(tag2);
			tagRepository.save(tag3);
			//creazione CategoryNode
			CategoryNode culturale =  new CategoryNode("Culturale");
			categoryRepository.save(culturale);
			CategoryNode spirituale =  new CategoryNode("Spirituale");
			categoryRepository.save(spirituale);
			CategoryNode architetturale =  new CategoryNode("Architetturale");
			categoryRepository.save(architetturale);
			CategoryNode ristorazione =  new CategoryNode("Ristorazione");
			categoryRepository.save(ristorazione);
			CategoryNode naturalistica =  new CategoryNode("Naturalistica");
			categoryRepository.save(naturalistica);
			CategoryNode fontanella =  new CategoryNode("Fontanella");
			categoryRepository.save(fontanella);
			CategoryNode zonaParcheggio =  new CategoryNode("ZonaParcheggio");
			categoryRepository.save(zonaParcheggio);
			CategoryNode mobilita =  new CategoryNode("Mobilita");
			categoryRepository.save(mobilita);
			//Crezione PoiType con Tags
			PoiTypeNode chiesa = new PoiTypeNode("Chiesa",Arrays.asList(culturale,spirituale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(chiesa);

			PoiTypeNode monastero = new PoiTypeNode("Monastero",Arrays.asList(culturale,spirituale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(monastero);

			PoiTypeNode rocca = new PoiTypeNode("Rocca",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(rocca);

			PoiTypeNode palazzo = new PoiTypeNode("Palazzo",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(palazzo);

			PoiTypeNode tempio = new PoiTypeNode("Tempio",Arrays.asList(culturale,spirituale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(tempio);

			PoiTypeNode santuario = new PoiTypeNode("Santuario",Arrays.asList(culturale,spirituale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(santuario);

			PoiTypeNode cattedrale = new PoiTypeNode("Cattedrale",Arrays.asList(culturale,spirituale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(cattedrale);

			PoiTypeNode basilica = new PoiTypeNode("Basilica",Arrays.asList(culturale,spirituale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(basilica);

			PoiTypeNode lago = new PoiTypeNode("Lago",Arrays.asList(naturalistica),Arrays.asList(tag1,tag2));
			poiTypeRepository.save(lago);

			PoiTypeNode biblioteca = new PoiTypeNode("Biblioteca",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(biblioteca);

			PoiTypeNode teatro = new PoiTypeNode("Teatro",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(teatro);

			PoiTypeNode mulino = new PoiTypeNode("Mulino",Arrays.asList(culturale,architetturale,naturalistica),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(mulino);

			PoiTypeNode giardino = new PoiTypeNode("Giardino",Arrays.asList(culturale,architetturale,naturalistica),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(giardino);

			PoiTypeNode statua = new PoiTypeNode("Statua",Arrays.asList(culturale),Arrays.asList(tag1,tag2));
			poiTypeRepository.save(statua);

			PoiTypeNode museo = new PoiTypeNode("Museo",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(museo);

			PoiTypeNode ristorante = new PoiTypeNode("Ristorante",Arrays.asList(ristorazione),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(ristorante);

			PoiTypeNode parco = new PoiTypeNode("Parco",Arrays.asList(naturalistica),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(parco);

			PoiTypeNode enoteca = new PoiTypeNode("Enoteca",Arrays.asList(naturalistica,ristorazione),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(enoteca);

			PoiTypeNode bosco = new PoiTypeNode("Bosco",Arrays.asList(naturalistica),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(bosco);

			PoiTypeNode parcoGiochi = new PoiTypeNode("Parco Giochi",Arrays.asList(naturalistica),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(parcoGiochi);

			PoiTypeNode piazza = new PoiTypeNode("Piazza",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(piazza);

			PoiTypeNode monumento = new PoiTypeNode("Monumento",Arrays.asList(culturale,architetturale),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(monumento);

			PoiTypeNode sostaCamper = new PoiTypeNode("Sosta Camper",Arrays.asList(zonaParcheggio),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(sostaCamper);

			PoiTypeNode sostaMacchine = new PoiTypeNode("Sosta Macchine",Arrays.asList(zonaParcheggio),
					Arrays.asList(tag1,tag2));
			poiTypeRepository.save(sostaMacchine);

			//setup per TimeSlot
			Map<String, List<LocalTime>> orari = new HashMap<>();
			orari.put("Monday",new ArrayList<>());
			orari.put("Tuesday",new ArrayList<>());
			orari.put("Wednesday",new ArrayList<>());
			orari.put("Thursday",new ArrayList<>());
			orari.put("Friday",new ArrayList<>());
			orari.put("Saturday",new ArrayList<>());
			orari.put("Sunday",new ArrayList<>());
			orari.values().forEach(localTimes -> {
						localTimes.add(LocalTime.parse("08:00"));
						localTimes.add(LocalTime.parse("13:00"));
						localTimes.add(LocalTime.parse("14:00"));
						localTimes.add(LocalTime.parse("20:00"));
					}
			);

			CityNode camerino = new CityNode("Camerino",camerinoCoords);
			cityRepository.save(camerino);
			this.createPoiProva(coordsRepository, poiRepository, "Monastero di S. Chiara",
					camerino, monastero, new CoordsNode(43.1392,13.0732),orari,timeSlotRepository,contactRepository,
					addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1483,13.102);
			this.createPoiProva(coordsRepository, poiRepository,"Chiesa e Convento dei Cappuccini di Renacavata",
					camerino, monastero,new CoordsNode(43.1483,13.102),
					orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.131,13.063);
			this.createPoiProva(coordsRepository, poiRepository, "Rocca Borgesca",camerino, rocca,
					new CoordsNode(43.131,13.063), orari, timeSlotRepository, contactRepository,
					addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1885,13.0638);
			this.createPoiProva(coordsRepository, poiRepository, "Rocca d'Ajello",camerino, rocca,
					new CoordsNode(43.1885,13.0638), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1103,13.1258);
			this.createPoiProva(coordsRepository, poiRepository, "Rocca Varano",camerino, rocca,
					new CoordsNode(43.1103,13.1258), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1357,13.0687);
			this.createPoiProva(coordsRepository, poiRepository, "Palazzo Ducale dei Da Varano",camerino,
					palazzo, new CoordsNode(43.1357,13.0687), orari, timeSlotRepository, contactRepository,
					addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1373,13.0724);
			this.createPoiProva(coordsRepository, poiRepository, "Tempio dell'Annunziata",camerino, tempio,
					new CoordsNode(43.1373,13.0724), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1369,13.0671);
			this.createPoiProva(coordsRepository, poiRepository, "Area di sosta di Via Macario Muzio",
					camerino, sostaMacchine, new CoordsNode(43.1369,13.0671), orari, timeSlotRepository,
					contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1393,13.0727);
			this.createPoiProva(coordsRepository, poiRepository, "Chiesa di S. Chiara",camerino, chiesa,
					new CoordsNode(43.1393,13.0727), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1358,13.0684);
			this.createPoiProva(coordsRepository, poiRepository, "Cattedrale di Santa Maria Annunziata",
					camerino, cattedrale, new CoordsNode(43.1358,13.0684), orari, timeSlotRepository,
					contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1347,13.0647);
			this.createPoiProva(coordsRepository, poiRepository, "Chiesa di S. Filippo Neri",camerino,
					chiesa, new CoordsNode(43.1347,13.0647), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1319,13.0638);
			this.createPoiProva(coordsRepository, poiRepository, "Santuario di S. Maria in Via",camerino,
					santuario, new CoordsNode(43.1319,13.0638), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1377,13.0736);
			this.createPoiProva(coordsRepository, poiRepository, "Basilica di S. Venanzio Martire",camerino,
					basilica, new CoordsNode(43.1377,13.0736), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1468,13.1303);
			this.createPoiProva(coordsRepository, poiRepository, "Santuario Maria Madre della Misericordia",
					camerino, santuario, new CoordsNode(43.1468,13.1303), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.0911,13.1165);
			this.createPoiProva(coordsRepository, poiRepository, "Lago di Polverina",camerino, lago,
					new CoordsNode(43.0911,13.1165), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.136,13.0692);
			this.createPoiProva(coordsRepository, poiRepository, "Biblioteca comunale Valentiniana",
					camerino, biblioteca, new CoordsNode(43.136,13.0692), orari, timeSlotRepository,
					contactRepository, addressRepository,cityRepository);

			//timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
			//		orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			//pointProvaCoords = new Coordinate(43.1658,13.0584);
			this.createPoiProva(coordsRepository, poiRepository, "Mulino Bottacchiari",camerino, mulino,
					new CoordsNode(43.1658,13.0584), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//pointProvaCoords = new Coordinate(43.1362,13.07);
			this.createPoiProva(coordsRepository, poiRepository,
					"Orto Botanico Carmela Cortini Università di Camerino",camerino, museo,
					new CoordsNode(43.1362,13.07),orari, timeSlotRepository, contactRepository,
					addressRepository,cityRepository);

			//pointProvaCoords = new Coordinate(43.1377,13.0713);
			this.createPoiProva(coordsRepository, poiRepository,
					"Polo museale di S. Domenico - Museo civico e archeologico – Pinacoteca civica Girolamo di Giovanni",
					camerino, museo, new CoordsNode(43.1377,13.0713), orari, timeSlotRepository,
					contactRepository, addressRepository,cityRepository);

			//pointProvaCoords = new Coordinate(43.1353,13.067);
			this.createPoiProva(coordsRepository, poiRepository, "Teatro Filippo Marchetti",camerino,
					teatro, new CoordsNode(43.1353,13.067), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//pointProvaCoords = new Coordinate(43.1885,13.0638);
			this.createPoiProva(coordsRepository, poiRepository,"Giardini della Rocca d'Ajello",camerino,
					giardino, new CoordsNode(43.1885,13.0638), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);

			//pointProvaCoords = new Coordinate(43.1358,13.0698);
			this.createPoiProva(coordsRepository, poiRepository, "Orto botanico di Camerino",camerino,
					giardino, new CoordsNode(43.1358,13.0698), orari, timeSlotRepository, contactRepository, addressRepository,cityRepository);
			//pointProvaCoords = new Coordinate(43.2104315,13.0526301);
			CityNode castel = new CityNode("Castel Raimondo",castelCoords);
			cityRepository.save(castel);
			this.createPoiProva(coordsRepository, poiRepository, "PalaSport Castel Raimondo",castel,palazzo,
					new CoordsNode(43.2104315,13.0526301), orari, timeSlotRepository, contactRepository,
					addressRepository,cityRepository);
			UserRoleNode enteRole = new UserRoleNode("ENTE");
			UserRoleNode touristRole = new UserRoleNode("TOURIST");
			UserRoleNode adminRole = new UserRoleNode("ADMIN");
			UserRoleNode thirdRole = new UserRoleNode("THIRD_PARTY");
			userRoleRepository.save(enteRole);
			userRoleRepository.save(touristRole);
			userRoleRepository.save(adminRole);
			userRoleRepository.save(thirdRole);
			*//*UserNode enteCamerino = new EnteNode("enteCamerino","ente_camerino",
					"ente.camerino@gmail.com","password","ente_camerino",camerino,enteRole);
			UserNode tourist = new TouristNode("name","surname",
					"name.surname@gmail.com","password","an_user",touristRole);
			//userNodeRepository.save(enteCamerino);
			//userNodeRepository.save(tourist);
			ItineraryNode it1 = new ItineraryNode("i1","desc1");
			ItineraryRelPoi rel1 = new ItineraryRelPoi(p1,1);
			ItineraryRelPoi rel2 = new ItineraryRelPoi(p2,2);
			ItineraryRelPoi rel3 = new ItineraryRelPoi(p3,3);
			it1.getPoints().add(rel1);
			it1.getPoints().add(rel2);
			it1.getPoints().add(rel3);
			itineraryRepository.save(it1);
			camerino.getItineraries().add(it1);
			cityRepository.save(camerino);*//*
		};
	}*/
}
