package com.yugal.store.services;

import com.yugal.store.conifg.JwtConfig;
import com.yugal.store.entities.Role;
import com.yugal.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    public String generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    public String generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder().subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(jwtConfig.getSecretKey()).compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException ex) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(jwtConfig.getSecretKey())
                .build().parseSignedClaims(token).getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }
}