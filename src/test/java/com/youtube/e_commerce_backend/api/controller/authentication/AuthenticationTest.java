package com.youtube.e_commerce_backend.api.controller.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.youtube.e_commerce_backend.api.model.RegistrationBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {

    @RegisterExtension
    public static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig()
                            .withUser("springboot", "secret"))
                    .withPerMethodLifecycle(true);
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void testRegister() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RegistrationBody body = new RegistrationBody();
        body.setUsername(null);
        body.setEmail("AuthenticationControllerTest@junit.com");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("Password123");
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));
        body.setUsername("");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));

        body.setUsername("AuthenticationControllerTest");
        body.setEmail(null);
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("Password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));
        body.setUsername("AuthenticationControllerTest");
        body.setEmail("");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("Password123");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));

        body.setEmail("AuthenticationControllerTest@junit.com");
        body.setPassword(null);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));
        body.setEmail("AuthenticationControllerTest@junit.com");
        body.setPassword("");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));

        body.setPassword("Password123");
        body.setFirstName(null);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));
        body.setPassword("Password123");
        body.setFirstName("");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));

        body.setFirstName("FirstName");
        body.setLastName(null);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));
        body.setFirstName("FirstName");
        body.setLastName("");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.BAD_REQUEST.value()));

        // TODO: Test password characters, username length, email validity
        body.setLastName("LastName");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(
                status().is(HttpStatus.OK.value()));



    }


}
