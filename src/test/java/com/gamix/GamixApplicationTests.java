package com.gamix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamixApplication.class)
public class GamixApplicationTests {
	@Test
	public void contextLoads() {}
	
    @Test
	public void integrationTestFlow() {
        // String url = "http://localhost:8080"; // Substitua pela URL correta do seu servidor GraphQL

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);

        // // Etapa 1: Registro de usuário
		// Map<String, String> signUpBody = new HashMap<>();
		// signUpBody.put("username", "user1");
		// signUpBody.put("email", "email@gmail.com");
		// signUpBody.put("password", "Password123!");
        // HttpEntity<String> signUpRequest = new HttpEntity<>(signUpBody.toString(), headers);

        // ResponseEntity<String> signUpResponseEntity = restTemplate.exchange(url+"/auth/signup", HttpMethod.POST, signUpRequest, String.class);
        // if (signUpResponseEntity.getStatusCode() == HttpStatus.OK) {
        //     String signUpResponseBody = signUpResponseEntity.getBody();
        //     System.out.println("SignUp Response: " + signUpResponseBody);
        // } else {
        //     System.err.println("Erro na solicitação de registro: " + signUpResponseEntity.getStatusCode());
        //     return; // Encerra o teste em caso de erro
        // }

        // // Etapa 2: Obter tokens e adicionar AccessToken ao header
        // String signUpResponseBody = signUpResponseEntity.getBody();
        // String accessToken = extractAccessToken(signUpResponseBody);
        // assertNotNull(accessToken);

        // headers.set("Authorization", "Bearer " + accessToken);

        // // Etapa 3: Executar o findAll com o AccessToken no header
        // String findAllQuery = "{ findAllUsers { username } }";
        // HttpEntity<String> findAllRequest = new HttpEntity<>(findAllQuery, headers);

        // ResponseEntity<String> findAllResponseEntity = restTemplate.exchange(url, HttpMethod.POST, findAllRequest, String.class);
        // if (findAllResponseEntity.getStatusCode() == HttpStatus.OK) {
        //     String findAllResponseBody = findAllResponseEntity.getBody();
        //     System.out.println("FindAll Response: " + findAllResponseBody);
        // } else {
        //     System.err.println("Erro na solicitação do findAll: " + findAllResponseEntity.getStatusCode());
        //     return; // Encerra o teste em caso de erro
        // }

        // // Etapa 4: Obter novos tokens após o findAll
        // String refreshToken = extractRefreshToken(signUpResponseBody);
        // assertNotNull(refreshToken);

        // String refreshMutation = "mutation { refreshTokenPasswodUser(refreshToken: \"" + refreshToken + "\") { accessToken refreshToken } }";
        // HttpEntity<String> refreshRequest = new HttpEntity<>(refreshMutation, headers);

        // ResponseEntity<String> refreshResponseEntity = restTemplate.exchange(url, HttpMethod.POST, refreshRequest, String.class);
        // if (refreshResponseEntity.getStatusCode() == HttpStatus.OK) {
        //     String refreshResponseBody = refreshResponseEntity.getBody();
        //     System.out.println("Refresh Response: " + refreshResponseBody);
        // } else {
        //     System.err.println("Erro na solicitação de refresh: " + refreshResponseEntity.getStatusCode());
        //     return; // Encerra o teste em caso de erro
        // }

        // // Etapa 5: Adicionar os tokens no header para o signOut
        // String newAccessToken = extractAccessToken(refreshResponseEntity.getBody());
        // assertNotNull(newAccessToken);

        // headers.set("Authorization", "Bearer " + newAccessToken);

        // String newRefreshToken = extractRefreshToken(refreshResponseEntity.getBody());
        // assertNotNull(newRefreshToken);

        // // Etapa 6: Fazer o signOut
        // String signOutMutation = "mutation { signOutPasswordUser(input: { accessToken: \"" + newAccessToken + "\", refreshToken: \"" + newRefreshToken + "\" }) }";
        // HttpEntity<String> signOutRequest = new HttpEntity<>(signOutMutation, headers);

        // ResponseEntity<String> signOutResponseEntity = restTemplate.exchange(url, HttpMethod.POST, signOutRequest, String.class);
        // if (signOutResponseEntity.getStatusCode() == HttpStatus.OK) {
        //     String signOutResponseBody = signOutResponseEntity.getBody();
        //     System.out.println("SignOut Response: " + signOutResponseBody);
        // } else {
        //     System.err.println("Erro na solicitação de signOut: " + signOutResponseEntity.getStatusCode());
        //     return; // Encerra o teste em caso de erro
        // }

        // // Etapa 7: Executar o findAll novamente após o signOut
        // ResponseEntity<String> findAllAfterSignOutResponseEntity = restTemplate.exchange(url, HttpMethod.POST, findAllRequest, String.class);
        // if (findAllAfterSignOutResponseEntity.getStatusCode() == HttpStatus.OK) {
        //     String findAllAfterSignOutResponseBody = findAllAfterSignOutResponseEntity.getBody();
        //     System.out.println("FindAll After SignOut Response: " + findAllAfterSignOutResponseBody);
        // } else {
        //     System.err.println("Erro na solicitação do findAll após o signOut: " + findAllAfterSignOutResponseEntity.getStatusCode());
        //     return; // Encerra o teste em caso de erro
        // }

        // // Etapa 8: Verificar se o usuário está desconectado
        // // Implemente a verificação aqui...

        // // Se o teste chegou até aqui sem erros, ele foi bem-sucedido
        // System.out.println("Teste de integração concluído com sucesso!");
    }

    // private String extractAccessToken(String response) {
    //     // Implemente o código para extrair o AccessToken do corpo da resposta GraphQL
    //     // (pode ser usando uma biblioteca de parsing JSON ou outro método)
    //     return null; // Substitua null pelo código de extração real
    // }

    // private String extractRefreshToken(String response) {
    //     // Implemente o código para extrair o RefreshToken do corpo da resposta GraphQL
    //     // (pode ser usando uma biblioteca de parsing JSON ou outro método)
    //     return null; // Substitua null pelo código de extração real
	// }
}
