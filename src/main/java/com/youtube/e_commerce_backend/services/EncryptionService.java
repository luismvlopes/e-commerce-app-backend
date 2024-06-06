package com.youtube.e_commerce_backend.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    @Value("${encryption.salt.round}")
    private int saltRounds;
    private String salt;

    @PostConstruct
    public void postConstruct() {
        this.salt = BCrypt.gensalt(saltRounds);
    }

    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }

}
