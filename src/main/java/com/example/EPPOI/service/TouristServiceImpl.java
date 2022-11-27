package com.example.EPPOI.service;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import net.bytebuddy.utility.RandomString;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TouristServiceImpl implements TouristService {

    private final ItineraryRepository itineraryRepository;
    private final CityRepository cityRepository;
    private final TouristRepository touristRepository;
    private final UserRoleRepository userRoleRepository;
    private final EnteRepository enteRepository;
    private final RequestPoiRepository requestPoiRepository;

    //email verification
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private CityNode getCityFromPoi(PoiNode poi){
        return this.cityRepository.findAll().stream().filter(c -> c.getPOIs().contains(poi)).findFirst()
                .orElseThrow(()-> new NullPointerException("No city found for that poi"));
    }

    private void addItineraryToUser(TouristNode tourist, ItineraryNode toAdd){
        tourist.getItineraries().add(toAdd);
        this.touristRepository.save(tourist);
    }


    @Override
    public TouristNode getUserByUsername(String username) throws NullPointerException {
        log.info("start ");
        UserRoleNode touristRole = this.userRoleRepository.findByName("TOURIST");
        log.info("role {}",touristRole);
        TouristNode user = this.touristRepository.findAll().stream().filter((u)-> u.getUsername().equals(username))
                .findFirst().orElseThrow(()-> new NullPointerException("No such user"));
        log.info("user {}",user);
        if(user.getRoles().contains(touristRole)) return user;
        else throw new NullPointerException("User " + username + "has not role " + touristRole);
    }

    //TODO: to modify
    @Override
    public void login(String username, String password) {

    }
    //TODO: to modify
    @Override
    public void logout(UUID uuid) {

    }

    /**
     *
     * @param user
     * @param siteURL
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    //metodo aggiunto per email verification
    public void register(TouristNode user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        touristRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(TouristNode user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        //TODO aggiorna dati se si cambia mail
        String toAddress = user.getEmail();
        String fromAddress = "eppoiProva2022@gmail.com";
        String senderName = "EPPOI";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "EPPOI.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName()+" "+user.getSurname());
        String verifyURL = siteURL + "/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public boolean verify(String verificationCode) {
        TouristNode user = this.touristRepository.findByVerificationCode(verificationCode);
        log.info("User: {}",user);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            this.touristRepository.save(user);

            return true;
        }

    }

    @Override
    public ItineraryNode createItinerary(TouristNode creator,String name, String description, List<PoiNode> POIs) {
        ItineraryNode result = new ItineraryNode(name, description);
        for (int i = 1; i <= POIs.size() ; i++) {
            PoiNode poi = POIs.get(i-1);
            result.getPoints().add(new ItineraryRelPoi(poi,i));
        }
        this.itineraryRepository.save(result);
        List<CityNode> cities = new ArrayList<>();
        for (PoiNode p : POIs) {
            cities.add(this.getCityFromPoi(p));
        }
        cities.forEach(cityNode -> {
            cityNode.getItineraries().add(result);
            //this.cityRepository.save(cityNode);
        });
        this.cityRepository.saveAll(cities);
        this.addItineraryToUser(creator,result);
        return result;
    }

    //FIXME: rivedere metodo
    @Override
    public RequestPoiNode createRequestPoi(TouristNode creator,PoiParamsProvider params,CityNode cityNode) {
        RequestPoiNode result = new RequestPoiNode(params,creator);
        this.requestPoiRepository.save(result);
        EnteNode enteNode = this.enteRepository.findAll().stream().filter(e-> e.getCity().equals(cityNode)).findFirst()
                .orElseThrow(()-> new NullPointerException("No such city"));
        enteNode.getPoiRequests().add(result);
        this.enteRepository.save(enteNode);
        creator.getPoiRequests().add(result);
        this.touristRepository.save(creator);
        return result;
    }

    @Override
    public RequestPoiNode createRequestPoi(TouristNode creator,PoiParamsProvider params, PoiNode toModify) {
        return null;
    }

    @Override
    public TouristRepository getRepository() {
        return this.touristRepository;
    }
}
