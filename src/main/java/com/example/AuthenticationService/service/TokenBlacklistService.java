package com.example.AuthenticationService.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.HashSet;

@Service
public class TokenBlacklistService {
    
    private final Set<String> blacklist = new HashSet<String>();

    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
