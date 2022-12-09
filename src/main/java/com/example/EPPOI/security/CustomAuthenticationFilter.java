package com.example.EPPOI.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, String> authenticate(String username, String password, String url) throws AuthenticationException {
        UsernamePasswordAuthenticationToken UPtoken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(UPtoken);
        User user = (User)auth.getPrincipal();
        String accessToken = jwtTokenProvider.generateJwtAccessToken(user,url);
        String refreshToken = jwtTokenProvider.generateJwtRefreshToken(user,url);
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",accessToken);
        tokens.put("refresh_token",refreshToken);
        return tokens;
    }
}
