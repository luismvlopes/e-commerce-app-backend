package com.youtube.e_commerce_backend.services;

import com.youtube.e_commerce_backend.exception.EmailFailureException;
import com.youtube.e_commerce_backend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${email.from}")
    private String fromAddress;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private SimpleMailMessage makeMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        return message;
    }

    public void sendVerificationEmail(VerificationToken token) throws EmailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(token.getUser().getEmail());
        message.setSubject("Verify your email to activate your account");
        message.setText("Please follow the link below to activate your account: \n"
                + frontendUrl + "/auth/verify?token=" + token.getToken());
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }
}
