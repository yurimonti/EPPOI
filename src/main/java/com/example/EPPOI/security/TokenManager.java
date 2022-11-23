package com.example.EPPOI.security;

import com.auth0.jwt.algorithms.Algorithm;

public class TokenManager {
    private final Algorithm accessAlgorithm;
    private final Algorithm refreshAlgorithm;

    //Value("${access_secret}")
    //private String accessSecret;
    //@Value("${refresh_secret}")
    //private String refreshSecret;

    private static TokenManager instance = null;

    private TokenManager() {
        this.accessAlgorithm = Algorithm.HMAC256("access_secret".getBytes());
        this.refreshAlgorithm = Algorithm.HMAC256("refresh_secret".getBytes());
    }

    public static TokenManager getInstance(){
        if(instance==null) return new TokenManager();
        else return instance;
    }

    public Algorithm getAccessAlgorithm() {
        return accessAlgorithm;
    }

    public Algorithm getRefreshAlgorithm() {
        return refreshAlgorithm;
    }
}
