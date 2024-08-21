package com.youtube.e_commerce_backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.youtube.e_commerce_backend.services.JwtTokenService;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private LocalUserDAO localUserDAO;

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Test
    public void testVerificationTokenNotUsableForLogin() {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();

        String verificationToken = jwtTokenService.generateVerificationToken(user);
        Assertions.assertNull(jwtTokenService.getUsername(verificationToken),
                "Verification token should not contain username");
    }


    @Test
    public void testAuthTokenReturnsUsername() {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtTokenService.generateToken(user);
        Assertions.assertEquals(user.getUsername(), jwtTokenService.getUsername(token),
                "Token for auth should contain user's username");
    }

    @Test
    public void testLoginJWTGeneratedByOutsiders() {
        String token =
                JWT.create().withClaim("USERNAME", "UserA")
                        .sign(Algorithm.HMAC256("NotRealSecretKey"));

        Assertions.assertThrows(
                SignatureVerificationException.class,
                () -> jwtTokenService.getUsername(token));
    }

    @Test
    public void testJLoginWTCorrectlySignedNoIssuer() {

        String token = JWT.create().withClaim("USERNAME", "UserA")
                .sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class, () -> jwtTokenService.getUsername(token));
    }

    @Test
    public void testPasswordResetToken() {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtTokenService.generatePasswordResetToken(user);
        Assertions.assertEquals(user.getEmail(), jwtTokenService.getResetPasswordEmail(token),
                "Token for password reset should contain user's email");
    }

    @Test
    public void testResetPasswordJWTGeneratedByOutsiders() {
        String token =
                JWT.create().withClaim("RESET_PASSWORD_EMAIL", "UserA@junit.com")
                        .sign(Algorithm.HMAC256("NotRealSecretKey"));

        Assertions.assertThrows(
                SignatureVerificationException.class,
                () -> jwtTokenService.getResetPasswordEmail(token));
    }

    @Test
    public void testJResetPasswordWTCorrectlySignedNoIssuer() {

        String token = JWT.create().withClaim("RESET_PASSWORD_EMAIL", "UserA@junit.com")
                .sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class, () -> jwtTokenService.getResetPasswordEmail(token));
    }
}
