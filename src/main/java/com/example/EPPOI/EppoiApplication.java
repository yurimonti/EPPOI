package com.example.EPPOI;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EppoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EppoiApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(TagRepository tagRepository, CityRepository cityRepository,
								   PoiRepository poiRepository, CoordsRepository coordsRepository,
								   ItineraryRepository itineraryRepository,UserRoleRepository userRoleRepository,
								   UserNodeRepository userNodeRepository,RequestPoiRepository requestPoiRepository){
		return args -> {
			requestPoiRepository.deleteAll();
			userNodeRepository.deleteAll();
			userRoleRepository.deleteAll();
			itineraryRepository.deleteAll();
			tagRepository.deleteAll();
			cityRepository.deleteAll();
            poiRepository.deleteAll();
			coordsRepository.deleteAll();
			CoordsNode camerinoCoords = new CoordsNode(20.2333,25.26666);
			coordsRepository.save(camerinoCoords);
			PoiNode p1 = new PoiNode("p1","desc1",20.00);
			PoiNode p2 = new PoiNode("p2","desc2",20.00);
			PoiNode p3 = new PoiNode("p3","desc3",20.00);
			PoiNode p4 = new PoiNode("p4","desc4",20.00);
			PoiNode p5 = new PoiNode("p5","desc5",20.00);
			PoiNode p6 = new PoiNode("p6","desc6",20.00);
			PoiNode p7 = new PoiNode("p7","desc7",20.00);
			PoiNode p8 = new PoiNode("p8","desc8",20.00);
			PoiNode p9 = new PoiNode("p9","desc9",20.00);
			poiRepository.save(p1);
			poiRepository.save(p2);
			poiRepository.save(p3);
			poiRepository.save(p4);
			poiRepository.save(p5);
			poiRepository.save(p6);
			poiRepository.save(p7);
			poiRepository.save(p8);
			poiRepository.save(p9);
			CityNode camerino = new CityNode("Camerino",camerinoCoords);
			camerino.getPOIs().add(p1);
			camerino.getPOIs().add(p2);
			camerino.getPOIs().add(p3);
			camerino.getPOIs().add(p4);
			camerino.getPOIs().add(p5);
			camerino.getPOIs().add(p6);
			camerino.getPOIs().add(p7);
			camerino.getPOIs().add(p8);
			camerino.getPOIs().add(p9);
			cityRepository.save(camerino);
			UserRoleNode enteRole = new UserRoleNode("ENTE");
			UserRoleNode touristRole = new UserRoleNode("TOURIST");
			UserRoleNode adminRole = new UserRoleNode("ADMIN");
			userRoleRepository.save(enteRole);
			userRoleRepository.save(touristRole);
			userRoleRepository.save(adminRole);
			UserNode enteCamerino = new EnteNode("enteCamerino","ente_camerino",
					"ente.camerino@gmail.com","password","ente_camerino",camerino,enteRole);
			UserNode tourist = new TouristNode("name","surname",
					"name.surname@gmail.com","password","an_user",touristRole);
			userNodeRepository.save(enteCamerino);
			userNodeRepository.save(tourist);
			ItineraryNode it1 = new ItineraryNode("i1","desc1");
			ItineraryRelPoi rel1 = new ItineraryRelPoi(p1,1);
			ItineraryRelPoi rel2 = new ItineraryRelPoi(p2,2);
			ItineraryRelPoi rel3 = new ItineraryRelPoi(p3,3);
			it1.getPoints().add(rel1);
			it1.getPoints().add(rel2);
			it1.getPoints().add(rel3);
			itineraryRepository.save(it1);
			camerino.getItineraries().add(it1);
			cityRepository.save(camerino);
		};
	}
}
