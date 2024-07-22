package com.youtube.e_commerce_backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.youtube.e_commerce_backend.services.EncryptionService;

@SpringBootTest
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void testEncryptPassword() {
        String password = "MySecretPassword123";
        String hashedPassword = encryptionService.encryptPassword(password);
        Assertions.assertTrue(encryptionService.verifyPassword(password, hashedPassword),
                "Hashed password should match original password");
        Assertions.assertFalse(encryptionService.verifyPassword("wrongPassword", hashedPassword),
                "Hashed password should not match original password");
    }


}
