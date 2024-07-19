package com.youtube.e_commerce_backend.services;

import com.youtube.e_commerce_backend.api.model.LoginBody;
import com.youtube.e_commerce_backend.api.model.RegistrationBody;
import com.youtube.e_commerce_backend.exception.EmailFailureException;
import com.youtube.e_commerce_backend.exception.UserAlreadyExistsException;
import com.youtube.e_commerce_backend.exception.UserNotVerifiedException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.VerificationToken;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import com.youtube.e_commerce_backend.model.dao.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private VerificationTokenDAO verificationTokenDAO;
    private EncryptionService encryptionService;
    private JwtTokenService jwtService;
    private EmailService emailService;

    public UserService(LocalUserDAO localUserDAO,
                       JwtTokenService jwtService,
                       EncryptionService encryptionService,
                       EmailService emailService,
                       VerificationTokenDAO verificationTokenDAO) {
        this.verificationTokenDAO = verificationTokenDAO;
        this.emailService = emailService;
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user using the provided registration details.
     *
     * @param  registrationBody   the registration details of the user
     * @return                   the newly registered user
     * @throws UserAlreadyExistsException  if the user already exists
     * @throws EmailFailureException       if there is an issue with email sending
     */
    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {

        if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
            || localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        localUserDAO.save(user);
        VerificationToken token = createVerificationToken(user);
        emailService.sendVerificationEmail(token);
        verificationTokenDAO.save(token);
        return user;
    }

    /**
     * Creates a verification token for the provided user.
     *
     * @param  user   the user for whom the verification token is created
     * @return       the generated verification token
     */
    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken token = new VerificationToken();
        token.setToken(jwtService.generateVerificationToken(user));
        token.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        token.setUser(user);
        user.getVerificationTokens().add(token);
        return token;
    }

    /**
     * Logs in a user based on the provided login details.
     *
     * @param  loginBody   the login details of the user
     * @return             the generated JWT token upon successful login
     * @throws UserNotVerifiedException  if the user is not verified
     * @throws EmailFailureException      if there is an issue with email sending during login
     */
    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> optUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (optUser.isPresent() && encryptionService.verifyPassword(loginBody.getPassword(), optUser.get().getPassword())) {
            LocalUser user = optUser.get();
            if(user.getIsEmailVerified()) {
            return jwtService.generateToken(user);
             } else {
                List<VerificationToken> verificationTokens = user.getVerificationTokens();
                boolean sendAgain = verificationTokens.isEmpty() ||
                        verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 ));
                if(sendAgain) {
                    VerificationToken token = createVerificationToken(user);
                    verificationTokenDAO.save(token);
                    emailService.sendVerificationEmail(token);
                }
                throw new UserNotVerifiedException(sendAgain);
            }
        }
        System.out.println("Login failed");
        return null;
    }

    @Transactional
    public boolean verifyUserEmail(String token) {
        Optional<VerificationToken> optToken = verificationTokenDAO.findByToken(token);
        if(optToken.isPresent()) {
            VerificationToken verificationToken = optToken.get();
            LocalUser user = verificationToken.getUser();
            if(!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }
}
