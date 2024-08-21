

package com.example.AuthenticationService.util;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.AuthenticationService.dto.AuthRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service 
public class jwtToken {
    private String secretKey="asdSJJHDJI6HYagffvvlvfmvfhyyeSBHBSDHuejbbe137y7ybhb3766673";

    public String generateToken(UserDetails req) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, req.getUsername());
    }
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateRefreshToken(String token,UserDetails userDetails) {
        return !isTokenExpired(token) && validateToken(token,userDetails);
    }

    public String generateTokenFromRefreshToken(String refreshToken,UserDetails userDetails) {
        return generateToken(userDetails);
    }
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignInKey(),Jwts.SIG.HS256).compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = getSignInKey();
        return (Jwts.parser().verifyWith(key)).build().parseSignedClaims(token).getPayload();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}