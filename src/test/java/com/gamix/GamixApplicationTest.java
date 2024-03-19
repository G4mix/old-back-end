package com.gamix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamix.communication.userController.SignInUserInput;
import com.gamix.communication.userController.SignUpUserInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = GamixApplication.class)
@AutoConfigureMockMvc
class GamixApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void integrationTestFlow() throws Exception {
        // Registrando o usuário

        SignUpUserInput signUpUserInput =
                new SignUpUserInput("test_user", "test_user@gmail.com", "Password123!");
        MvcResult signUpResult = mockMvc.perform(post("/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(signUpUserInput)))
                .andExpect(status().isOk()).andReturn();

        String token = "Bearer "+Objects.requireNonNull(signUpResult.getResponse().getHeader("Set-Cookie")).split(";")[0].split("=")[1];
        assertNotNull(token);

        // Executar o findAll com o AccessToken no header
        // Criando o corpo da requisição
        Map<String, Object> findAllRequestBody = new HashMap<>();
        findAllRequestBody.put("query",
                "query FindAllUsers($skip: Int, $limit: Int) {"+
                "findAllUsers(skip: $skip, limit: $limit) { id } }");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("skip", 0);
        variables.put("limit", 10);
        findAllRequestBody.put("variables", variables);

        mockMvc.perform(post("/graphql").contentType("application/json")
                .content(objectMapper.writeValueAsString(findAllRequestBody))
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        // Fazendo o signIn
        SignInUserInput signInUserInput =
                new SignInUserInput(signUpUserInput.username(), null,
                        signUpUserInput.password(), false);
        MvcResult signInResult = mockMvc
                .perform(post("/auth/signin").contentType("application/json")
                        .content(objectMapper.writeValueAsString(signInUserInput)))
                .andExpect(status().isOk()).andReturn();

        token = "Bearer "+Objects.requireNonNull(signUpResult.getResponse().getHeader("Set-Cookie")).split(";")[0].split("=")[1];
        assertNotNull(token);

        // Executar o findAll com o novo AccessToken no header
        mockMvc.perform(post("/graphql").contentType("application/json")
                .content(objectMapper.writeValueAsString(findAllRequestBody))
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }
}
