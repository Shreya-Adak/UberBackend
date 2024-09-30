package com.codingshuttle.project.uber.UberAppBackend.security;

import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.secretKey}")
    private String Key;

    //for creating key object
    private SecretKey getSecretKeyObject(){

        return Keys.hmacShaKeyFor(Key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){   //6.1 vdo

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                //.claim("roles", Set.of("ADMIN","USER"))
                .claim("roles", user.getRoles()) //6.4vdo
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                // .expiration(new Date(System.currentTimeMillis() + 1000*20))
                .signWith(getSecretKeyObject())
                .compact();
    }

    public Long getUserIdFromToken(String token){

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKeyObject())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }

    public String generateRefreshToken(User user){   //6.1 vdo

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L *60*60*24*30*6))
                //.expiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(getSecretKeyObject())
                .compact();
    }
}
