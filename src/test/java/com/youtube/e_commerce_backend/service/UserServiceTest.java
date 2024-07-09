package com.youtube.e_commerce_backend.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.youtube.e_commerce_backend.api.model.RegistrationBody;
import com.youtube.e_commerce_backend.exception.UserAlreadyExistsException;
import com.youtube.e_commerce_backend.services.UserService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

//@Disabled
@SpringBootTest
public class UserServiceTest {

    @RegisterExtension
    public static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig()
                            .withUser("springboot", "secret"))
                            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

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

}
