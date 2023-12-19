package com.gamix;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamix.communication.passwordUserController.SignInPasswordUserInput;
import com.gamix.communication.passwordUserController.SignUpPasswordUserInput;

@SpringBootTest(classes = GamixApplication.class)
@AutoConfigureMockMvc
class GamixApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void integrationTestFlow() throws Exception {
		// Registrando o usuário

		SignUpPasswordUserInput signUpPasswordUserInput =
				new SignUpPasswordUserInput("example_user", "example@gmail.com", "Password123!");
		MvcResult signUpResult = mockMvc
				.perform(post("/auth/signup").contentType("application/json")
						.content(objectMapper.writeValueAsString(signUpPasswordUserInput)))
				.andExpect(status().isOk()).andReturn();

		String signUpResponseBody = signUpResult.getResponse().getContentAsString();

		JsonNode jsonNode = objectMapper.readTree(signUpResponseBody);

		String accessTokenCookie = jsonNode.get("accessToken").asText();
		String refreshTokenCookie = jsonNode.get("refreshToken").asText();
		String accessToken = extractTokenValue(accessTokenCookie, "accessToken");
        extractTokenValue(refreshTokenCookie, "refreshToken");
        String refreshToken;

		// Executar o findAll com o AccessToken no header

		// Criando o corpo da requisição
		Map<String, Object> findAllRequestBody = new HashMap<>();
		findAllRequestBody.put("query",
				"query FindAllUsers($skip: Int, $limit: Int) { findAllUsers(skip: $skip, limit: $limit) { id username email icon passwordUser { id verifiedEmail } } }");
		Map<String, Integer> variables = new HashMap<>();
		variables.put("skip", 0);
		variables.put("limit", 10);
		findAllRequestBody.put("variables", variables);

		mockMvc.perform(post("/graphql").contentType("application/json")
				.content(objectMapper.writeValueAsString(findAllRequestBody))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
				.andExpect(status().isOk());

		// Fazendo o signIn
		SignInPasswordUserInput signInPasswordUserInput =
				new SignInPasswordUserInput(signUpPasswordUserInput.username(), null,
						signUpPasswordUserInput.password(), false);
		MvcResult signInResult = mockMvc
				.perform(post("/auth/signin").contentType("application/json")
						.content(objectMapper.writeValueAsString(signInPasswordUserInput)))
				.andExpect(status().isOk()).andReturn();

		String signInResponseBody = signInResult.getResponse().getContentAsString();

		jsonNode = objectMapper.readTree(signInResponseBody);

		accessTokenCookie = jsonNode.get("accessToken").asText();
		refreshTokenCookie = jsonNode.get("refreshToken").asText();
		accessToken = extractTokenValue(accessTokenCookie, "accessToken");
		refreshToken = extractTokenValue(refreshTokenCookie, "refreshToken");

		// Executar o findAll com o novo AccessToken no header
		mockMvc.perform(post("/graphql").contentType("application/json")
				.content(objectMapper.writeValueAsString(findAllRequestBody))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
				.andExpect(status().isOk());

		// Usar o refreshToken
		// Criando o corpo da requisição
		Map<String, Object> refreshTokenRequestBody = new HashMap<>();
		refreshTokenRequestBody.put("refreshToken", refreshToken);

		MvcResult refreshTokenResult = mockMvc
				.perform(post("/auth/refreshtoken").contentType("application/json")
						.content(objectMapper.writeValueAsString(refreshTokenRequestBody)))
				.andExpect(status().isOk()).andReturn();

		String refreshTokenResponseBody = refreshTokenResult.getResponse().getContentAsString();

		jsonNode = objectMapper.readTree(refreshTokenResponseBody);

		accessTokenCookie = jsonNode.get("accessToken").asText();
		refreshTokenCookie = jsonNode.get("refreshToken").asText();
		accessToken = extractTokenValue(accessTokenCookie, "accessToken");
        extractTokenValue(refreshTokenCookie, "refreshToken");

        // Executar o findAll com o novo AccessToken no header
		mockMvc.perform(post("/graphql").contentType("application/json")
				.content(objectMapper.writeValueAsString(findAllRequestBody))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
				.andExpect(status().isOk());
	}

	private String extractTokenValue(String cookieValue, String field) {
		String regex = field + "=(.*?);";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cookieValue);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return "";
	}

}
