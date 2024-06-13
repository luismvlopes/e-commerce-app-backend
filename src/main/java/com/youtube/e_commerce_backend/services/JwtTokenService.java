package com.youtube.e_commerce_backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.youtube.e_commerce_backend.model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expirationInSeconds}")
    private String expirationInSeconds;
    private Algorithm algorithm;
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL_KEY = "EMAIL_KEY";

    @PostConstruct
    public void postConstruct() {
        this.algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(LocalUser user) {
        return JWT.create()
                .withClaim(USERNAME, user.getUsername())
                .withIssuer(issuer)
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1000 + Long.parseLong(expirationInSeconds))))
                .sign(algorithm);
    }

    public String getUsername(String token) {
        return JWT.decode(token).getClaim(USERNAME).asString();
    }

    public String generateVerificationToken(LocalUser user) {
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withIssuer(issuer)
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1000 + Long.parseLong(expirationInSeconds))))
                .sign(algorithm);
    }
}
