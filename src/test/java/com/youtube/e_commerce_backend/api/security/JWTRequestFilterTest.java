package com.youtube.e_commerce_backend.api.security;

import com.youtube.e_commerce_backend.model.LocalUser;
import com.youtube.e_commerce_backend.model.dao.LocalUserDAO;
import com.youtube.e_commerce_backend.services.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private LocalUserDAO localUserDAO;
    private static final String AUTHENTICATED_PATH = "/auth/me";

    @Test
    public void testUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get(AUTHENTICATED_PATH)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testBadToken() throws Exception {
        mockMvc.perform(get(AUTHENTICATED_PATH)
                .header("Authorization", "BadTokenNotValid")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        mockMvc.perform(get(AUTHENTICATED_PATH)
                .header("Authorization", "Bearer BadTokenNotValid")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testUnverifiedUserEmail() throws Exception {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserB").get();
        String token = jwtTokenService.generateToken(user);
        mockMvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testSuccessful() throws Exception {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtTokenService.generateToken(user);
        mockMvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }
}
