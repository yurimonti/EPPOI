package com.example.EPPOI.service;

import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GeneralUserService {
    UserNode saveUser(UserNode user);

    void saveUsers(List<UserNode> users);
    UserRoleNode saveRole(UserRoleNode role);
    void addRoleToUser(UserNode user, UserRoleNode role);
    UserNode getUser(String userName);
    List<UserNode> getUsers();
}
