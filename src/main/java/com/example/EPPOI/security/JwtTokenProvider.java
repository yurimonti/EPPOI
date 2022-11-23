package com.example.EPPOI.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtTokenProvider {

    public String generateJwtAccessToken(User user, String url) {
        TokenManager tokenManager = TokenManager.getInstance();
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()*1000*60*5))
                .withIssuer(url)
                .withClaim("role",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(tokenManager.getAccessAlgorithm());
    }
    public String generateJwtRefreshToken(User user, String url) {
        TokenManager tokenManager = TokenManager.getInstance();
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()*1000*3600*24*7))
                .withIssuer(url)
                .withClaim("role",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(tokenManager.getRefreshAlgorithm());
    }

    // permit to get authorities from the token
/*    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).toList().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }*/

    /*public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(username, null, authorities);
        // set details of users in spring security details
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }*/

/*    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return !username.isEmpty() && !isTokenExpired(verifier, token);
    }

    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }*/

    /*private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }*/

/*    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim("role").asArray(String.class);
    }*/

/*    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = HMAC512("TOKEN_SECRET");
            verifier = JWT.require(algorithm).withIssuer(request.getRequestURL().toString()).build();
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException("TOKEN_CANNOT_BE_VERIFIED");
        }
        return verifier;
    }*/

    /*private String[] getClaimsFromUser(User user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities() ){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }*/
}
