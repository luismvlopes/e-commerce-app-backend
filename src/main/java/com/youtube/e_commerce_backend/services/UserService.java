package com.youtube.e_commerce_backend.services;

import com.youtube.e_commerce_backend.api.model.LoginBody;
import com.youtube.e_commerce_backend.api.model.RegistrationBody;
import com.youtube.e_commerce_backend.exception.UserAlreadyExistsException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserRepository;
    private EncryptionService encryptionService;
    private JwtTokenService jwtService;

    public UserService(LocalUserDAO localUserDAO,
                       JwtTokenService jwtService,
                       EncryptionService encryptionService) {
        this.localUserRepository = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if (localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
            || localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        user.setEmail(registrationBody.getEmail());

        return localUserRepository.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> optUser = localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (optUser.isPresent() && encryptionService.verifyPassword(loginBody.getPassword(), optUser.get().getPassword())) {
            return jwtService.generateToken(optUser.get());
        }
        // TODO handle login failure?
        System.out.println("Login failed");
        return null;
    }
}
