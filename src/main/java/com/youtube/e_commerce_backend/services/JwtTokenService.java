package com.youtube.e_commerce_backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    private static final String VERIFICATION_EMAIL_KEY = "VERIFICATION_EMAIL";
    private static final String RESET_PASSWORD_EMAIL_KEY = "RESET_PASSWORD_EMAIL";


    @PostConstruct
    public void initAlgorithm() {
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
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return jwt.getClaim(USERNAME).asString();
    }

    public String generateVerificationToken(LocalUser user) {
        return JWT.create()
                .withClaim(VERIFICATION_EMAIL_KEY, user.getEmail())
                .withIssuer(issuer)
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1000 + Long.parseLong(expirationInSeconds))))
                .sign(algorithm);
    }

    public String generatePasswordResetToken(LocalUser user) {
        return JWT.create()
                .withClaim(RESET_PASSWORD_EMAIL_KEY, user.getEmail())
                .withIssuer(issuer)
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1000 + 60 * 30)))
                .sign(algorithm);
    }

    public String getResetPasswordEmail(String token) {
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return jwt.getClaim(RESET_PASSWORD_EMAIL_KEY).asString();
    }
}
