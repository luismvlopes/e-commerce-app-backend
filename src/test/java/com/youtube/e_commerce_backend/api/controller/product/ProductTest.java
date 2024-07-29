package com.youtube.e_commerce_backend.api.controller.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {

   @Autowired
   private MockMvc mockMvc;

   @Test
    public void testProductList() throws Exception {
       mockMvc.perform(get("/product")).andExpect(
               status().is(HttpStatus.OK.value()));
   }
}
