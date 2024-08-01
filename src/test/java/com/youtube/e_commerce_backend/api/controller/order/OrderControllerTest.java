package com.youtube.e_commerce_backend.api.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtube.e_commerce_backend.model.WebOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("UserA")
    public void testUserAAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongingToUser("UserA");
    }

    @Test
    @WithUserDetails("UserB")
    public void testUserBAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongingToUser("UserB");
    }

    private  void testAuthenticatedListBelongingToUser(String username) throws Exception {
        mockMvc.perform(get("/order")).andExpect(
                status().is(HttpStatus.OK.value()))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<WebOrder> orders = new ObjectMapper().readValue(json, new TypeReference<List<WebOrder>>() {});
                    for (WebOrder order : orders) {
                        Assertions.assertEquals(username, order.getUser().getUsername(),
                                "Order list should contain orders from UserA only");
                    }
                });
    }

    @Test
    public void TestUnauthenticatedOrderList() throws Exception {
        mockMvc.perform(get("/order")).andExpect(
                status().is(HttpStatus.FORBIDDEN.value()));
    }


}
