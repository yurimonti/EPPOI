package com.example.EPPOI.service;

import com.example.EPPOI.model.user.UserNode;

import java.util.UUID;

public interface AppUserService<T extends UserNode> {
    T getUserByUsername(String username);
    //TODO: to modify
    void login(String username, String password);
    void logout(UUID uuid);
}
