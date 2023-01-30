package com.example.EPPOI.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCycleImpl implements TokenCycleService{
    private final TokenCyclerRepository tokenCyclerRepository;
    @Override
    public RefreshTokenCycler getCyclerInstance() {
        return this.tokenCyclerRepository.findAll().stream().findFirst().orElseGet(() -> {
            RefreshTokenCycler r = new RefreshTokenCycler();
            this.tokenCyclerRepository.save(r);
            return r;
        });
    }

    @Override
    public boolean containsToken(String token) {
        return this.getCyclerInstance().getRefreshTokens().contains(token);
    }

    @Override
    public void addToken(String token) {
        if(!this.containsToken(token)) {
            RefreshTokenCycler refresher = this.getCyclerInstance();
            refresher.getRefreshTokens().add(token);
            this.tokenCyclerRepository.save(refresher);
        }
        log.info("Added token {}",token);
    }

    @Override
    public void removeToken(String token) {
        if(this.containsToken(token)) {
            RefreshTokenCycler refresher = this.getCyclerInstance();
            refresher.getRefreshTokens().remove(token);
            this.tokenCyclerRepository.save(refresher);
        }
    }
}
