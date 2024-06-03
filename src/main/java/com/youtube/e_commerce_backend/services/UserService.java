package com.youtube.e_commerce_backend.services;

import com.youtube.e_commerce_backend.api.model.RegistrationBody;
import com.youtube.e_commerce_backend.exception.UserAlreadyExistsException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.repository.LocalUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private LocalUserRepository localUserRepository;

    public UserService(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
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
        // TODO: encrypt password
        user.setPassword(registrationBody.getPassword());
        user.setEmail(registrationBody.getEmail());

        return localUserRepository.save(user);
    }
}
