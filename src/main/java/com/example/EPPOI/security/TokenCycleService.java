package com.example.EPPOI.security;

import org.springframework.stereotype.Service;

@Service
public interface TokenCycleService {
    RefreshTokenCycler getCyclerInstance();
    boolean containsToken(String token);
    void addToken(String token);
    void removeToken(String token);
}
