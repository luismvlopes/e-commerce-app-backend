package com.youtube.e_commerce_backend.service;

import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.youtube.e_commerce_backend.services.JwtTokenService;

@SpringBootTest
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private LocalUserDAO localUserDAO;

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

}
