package com.example.EPPOI.service;

import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.EnteRepository;
import com.example.EPPOI.repository.TouristRepository;
import com.example.EPPOI.repository.UserNodeRepository;
import com.example.EPPOI.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralUserServiceImpl implements GeneralUserService, UserDetailsService {
    private final UserNodeRepository userNodeRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserNode saveUser(UserNode user) {
        user.setPassword(this.passwordEncoder.encode((user.getPassword())));
        return this.userNodeRepository.save(user);
    }

    @Override
    public void saveUsers(List<UserNode> users) {
        this.userNodeRepository.saveAll(users);
    }

    @Override
    public UserRoleNode saveRole(UserRoleNode role) {
        return this.userRoleRepository.save(role);
    }

    @Override
    public void addRoleToUser(UserNode user, UserRoleNode role) {
        List<UserRoleNode> roles = user.getRoles();
        if (roles.stream().noneMatch(r -> r.equals(role))){
            roles.add(role);
            this.userNodeRepository.save(user);
        }
    }

    @Override
    public UserNode getUser(String username) {
        /*UserNode user = this.userNodeRepository.findByUsername(username);
        if(Objects.isNull(user)) throw new NullPointerException("User not found");
        return user;*/
        return this.userNodeRepository.findAll().stream().filter(u -> u.getUsername().equals(username)).findAny()
                .orElseThrow(()-> new NullPointerException("User not found"));
    }

    @Override
    public List<UserNode> getUsers() {
        return this.userNodeRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserNode user = getUser(username);
        if(user == null){
            log.error("User " + username + "not found in database");
            throw new UsernameNotFoundException("User " + username + "not found in database");
        } else {
            log.info("User {} found in database", username);
            log.info("ROLES {} for {}",user.getRoles(), username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(user.getUsername(),user.getPassword(),authorities);
    }
}
