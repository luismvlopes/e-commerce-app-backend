package com.youtube.e_commerce_backend.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.youtube.e_commerce_backend.api.model.LoginBody;
import com.youtube.e_commerce_backend.api.model.RegistrationBody;
import com.youtube.e_commerce_backend.api.model.ResetPasswordBody;
import com.youtube.e_commerce_backend.exception.EmailFailureException;
import com.youtube.e_commerce_backend.exception.EmailNotFoundException;
import com.youtube.e_commerce_backend.exception.UserAlreadyExistsException;
import com.youtube.e_commerce_backend.exception.UserNotVerifiedException;
import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.VerificationToken;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import com.youtube.e_commerce_backend.model.dao.VerificationTokenDAO;
import com.youtube.e_commerce_backend.services.EncryptionService;
import com.youtube.e_commerce_backend.services.JwtTokenService;
import com.youtube.e_commerce_backend.services.UserService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @RegisterExtension
    public static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig()
                            .withUser("springboot", "secret"))
                            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private VerificationTokenDAO verificationTokenDAO;
    @Autowired
    private LocalUserDAO localUserDAO;
    @Autowired
    private EncryptionService encryptionService;


    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        RegistrationBody body = new RegistrationBody();
        body.setUsername("UserA");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        body.setFirstName("testFirstName");
        body.setLastName("testLastName");
        body.setPassword("MySecretPassword123");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body),
                "Username should already be in use");

        body.setUsername("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");
        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body),
                "Email should already be in use");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(() -> userService.registerUser(body),
                "User should be able to register successfully");
        Assertions.assertEquals(body.getEmail(),
                greenMail.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, EmailFailureException {
        LoginBody body = new LoginBody();
        body.setUsername("UserA-NotExist");
        body.setPassword("PasswordA123-BadPassword");
        Assertions.assertNull(userService.loginUser(body), "The user should not exist");
        body.setUsername("UserA");
        body.setPassword("PasswordA123");
        Assertions.assertNotNull(userService.loginUser(body), "The password should be incorrect");
        body.setPassword("PasswordA123");
        Assertions.assertNotNull(userService.loginUser(body), "The user should login successfully");
        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try {
            userService.loginUser(body);
            Assertions.fail("The user should not have email verified");
        } catch (UserNotVerifiedException ex) {
            Assertions.assertTrue(ex.isNewEmailSent(), "Email verification should be sent");
            Assertions.assertEquals(1, greenMail.getReceivedMessages().length);
        }
        try {
            userService.loginUser(body);
            Assertions.fail("The user should not have email verified");
        } catch (UserNotVerifiedException ex) {
            Assertions.assertFalse(ex.isNewEmailSent(), "Email verification should be resent");
            Assertions.assertEquals(1, greenMail.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void testVerifyUser() throws EmailFailureException {
        Assertions.assertFalse(userService.verifyUserEmail("invalid_token"),
                    "This test token should not be valid and return false");
        LoginBody body = new LoginBody();
        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try {
            userService.loginUser(body);
            Assertions.fail("The user should not have email verified");
        } catch (UserNotVerifiedException ex) {
            List<VerificationToken> tokens = verificationTokenDAO.findByUser_IdOrderByUser_IdDesc(2L);
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(userService.verifyUserEmail(token), "The token should be valid and return true");
            Assertions.assertNotNull(body, "The user should be verified");
        }
    }

    @Test
    @Transactional
    public void testForgotPassword() throws MessagingException {
        Assertions.assertThrows(EmailNotFoundException.class,
                () -> userService.forgotPassword("UserNotExist@junit.com"),
                "This test email should not exist and throw an exception");
        Assertions.assertDoesNotThrow(() -> userService.forgotPassword("UserA@junit.com"),
                "Non existing email should not throw an exception");
        Assertions.assertEquals("UserA@junit.com",
                greenMail.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString(),
                "Email should be sent");
    }


    @Test
    public void testResetPassword() {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtTokenService.generatePasswordResetToken(user);
        ResetPasswordBody body = new ResetPasswordBody();
        body.setToken(token);
        body.setPassword("Password12345");
        userService.resetPassword(body);
        user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        Assertions.assertTrue(encryptionService.verifyPassword("Password12345", user.getPassword()), "Password should be reset");
    }

}
