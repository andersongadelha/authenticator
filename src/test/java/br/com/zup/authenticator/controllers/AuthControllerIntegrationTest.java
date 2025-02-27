package br.com.zup.authenticator.controllers;

import configuracao.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;


    @Autowired
    public AuthControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    @Test
    public void deveLogarComSucesso() throws Exception {
        String requestBody = """
            {
                "username": "testUser",
                "password": "testPassword"
            }
        """;
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    public void deveRetornarNaoAutorizado() throws Exception {
        String requestBody = """
            {
                "username": "usuarioInexistente",
                "password": "senhaQualquer"
            }
        """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }
}