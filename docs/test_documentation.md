# Documentação de Testes

Este arquivo registra uma visão geral dos testes realizados em diferentes classes do back-end do Gamix.


- [Documentação de Testes](#documentação-de-testes)
  - [initialization](#initialization)
    - [Classe SystemStartupService](#classe-systemstartupservice)
  - [security](#security)
    - [Classe JwtManagerTest](#classe-jwtmanagertest)
  - [service](#service)
    - [Classe InvalidTokenService](#classe-invalidtokenservice)
    - [Classe PasswordUserService](#classe-passworduserservice)
    - [Classe UserService](#classe-userservice)
  - [utils](#utils)
    - [Classe ParameterValidator](#classe-parametervalidator)

## initialization
### Classe SystemStartupService
- **Implementados**
  1. **Função: processUnbannedUsers**
  - Verifica se o método `processUnbannedUsers` funciona corretamente.
  
  2. **Função: processUnbannedTokens**
  - Verifica se o método `processUnbannedTokens` funciona corretamente.
  
  3. **Função: scheduleUnbanTasksForRemainingBannedUsers**
  - Verifica se o método `scheduleUnbanTasksForRemainingBannedUsers` funciona corretamente.
  
  4. **Função: scheduleUnbanTasksForRemainingBannedTokens**
  - Verifica se o método `scheduleUnbanTasksForRemainingBannedTokens` funciona corretamente.

- **Não implementados**

## security
### Classe JwtManagerTest
- **Implementados**
  1. **Função: getTokenClaims**
  - Verifica se a função `getTokenClaims` ao receber um token válido retorna as reivindicações do token corretamente.
  - Verifica se a função `getTokenClaims` lança uma exceção `TokenClaimsException` ao receber um token malformado.

  2. **Função: generateJwtTokens**
  - Verifica se a função `generateJwtTokens` ao receber os dados corretos, retorna corretamente os `JwtTokens`.

- **Não implementados**
  
## service
### Classe InvalidTokenService
- **Implementados**
- **Não implementados**
  1. **Função: addInvalidToken**
    - Verifica se a função `addInvalidToken` adiciona na blacklist o token, com o tempo correto, e agenda o momento em que vai ser deletado;
  2. **Função: isTokenOnBlacklist**
    - ...
  3. **Função: deleteInvalidToken**
    - ...
  4. **Função: findByExpirationTimeInSecondsLessThanEqual**
    - ...
  5. **Função: findAll**
    - ...

### Classe PasswordUserService
- **Implementados**
  1. **Função: createPasswordUser**
    - Verifica se a função `createPasswordUser` cria um novo usuário com senha e retorna o objeto `PasswordUser` criado.
    
  2. **Função: refreshToken**
    - Usa a função `refreshToken` com um refreshToken válido, e é esperado o retorno de novos `JwtTokens`.
    - Usa a função `refreshToken` com um refreshToken inválido, e lança a exceção `InvalidRefreshToken`.

  3. **Função: signInPasswordUser**
    - Usa a função `signInPasswordUser` com credenciais válidas usando o username, e é esperado o retorno de novos `JwtTokens`;
    - Usa a função `signInPasswordUser` com credenciais válidas usando o email, e é esperado o retorno de novos `JwtTokens`;
    - Usa a função `signInPasswordUser` usando um email que não existe, e é esperado uma exceção `UserNotFound`;
    - Usa a função `signInPasswordUser` usando um email que existe, mas não é um passwordUser, é esperado uma exceção `PasswordUserNotFound`;
    - Usa a função `signInPasswordUser` usando um email que existe, mas erra a senha lançando um erro `PasswordWrong`;
    - Usa a função `signInPasswordUser` usando um email que existe, mas erra a senha lançando um erro `PasswordWrong`, tenta novamente, e é bloqueado por tentativas excessivas lançando um erro `ExcessiveFailedLoginAttempts`, e após o tempo passar é desbanido;
    - Usa a função `signInPasswordUser` com credenciais válidas usando o email, mas de alguma forma os tokens JWT acabam sendo nulos, e é lançada uma exceção `Null`JwtTokens``.
  
  4. **Função: signOutPasswordUser**
    - Usa a função `signOutPasswordUser` com um accessToken e um refreshToken válidos, é esperado que ambos os tokens sejam adicionados em uma blacklist, e removidos dela assim que eles expiram naturalmente pelo tempo;
    - Usa a função `signOutPasswordUser` com um accessToken inválido, é esperado que uma exceção InvalidAccessToken seja lançada;
    - Usa a função `signOutPasswordUser` com um refreshToken inválido, é esperado que uma exceção InvalidRefreshToken seja lançada;
    - Usa a função `signOutPasswordUser` com um accessToken e um refreshToken válidos, mas com o id de ambos diferentes, ou seja pertencentes a usuários diferentes, é esperado que uma exceção TokensDoNotMatchException seja lançada.

  5. **Função: signUpPasswordUser**
    - Usa a função `signUpPasswordUser` com um input válido, e é esperado que os `JwtTokens` sejam retornados;
    - Usa a função `signUpPasswordUser` com um username de um usuário que já existe e é esperado que uma exceção `UserAlreadyExistsWithThisUsername` seja lançada;
    - Usa a função `signUpPasswordUser` com um email de um usuário que já existe e é esperado que uma exceção `UserAlreadyExistsWithThisEmail` seja lançada;
    - Usa a função `signUpPasswordUser` com um username inválido, realiza todos os testes de [validateUsername](#testes-de-validação-de-parâmetros)
    - Usa a função `signUpPasswordUser` com um email inválido, realiza todos os testes de [validateEmail](#testes-de-validação-de-parâmetros)
    - Usa a função `signUpPasswordUser` com uma password inválida, realiza todos os testes de [validatePassword](#testes-de-validação-de-parâmetros)
  
- **Não implementados**

### Classe UserService
- **Implementados**
  1. **Função: createUser**
    - Verifica se a função `createUser` cria um novo usuário com sucesso e retorna o objeto `User` criado.

  2. **Função: deleteAccount**
    - Verifica se a função `deleteAccount` deleta a conta do usuário com sucesso.
    - Verifica se a função `deleteAccount` lança uma exceção `InvalidAccessToken` quando o token de acesso é inválido.
    
  3. **Função: findAllUsers**
    - Verifica se a função `findAllUsers` retorna uma lista de usuários corretamente.

  4. **Função: findUserByEmail**
    - Verifica se a função `findUserByEmail` retorna o usuário correto a partir do e-mail.
    - Verifica se a função `findUserByEmail` lança uma exceção `UserNotFoundByEmail` quando o usuário não é encontrado com o e-mail.

  5. **Função: findUserByToken**
    - Verifica se a função `findUserByToken` retorna o usuário correto a partir do token de acesso.
    - Verifica se a função `findUserByToken` lança uma exceção `UserNotFoundByToken` quando o usuário não é encontrado com o token de acesso.

  6. **Função: findUserByUsername**
    - Verifica se a função `findUserByUsername` retorna o usuário correto a partir do nome de usuário.
    - Verifica se a função `findUserByUsername` lança uma exceção `UserNotFoundByUsername` quando o usuário não é encontrado com o nome de usuário.

  7. **Função: updateUser**
    - Verifica se a função `updateUser` atualiza os dados do usuário com sucesso.
    - Verifica se a função `updateUser` lança uma exceção `UserNotFoundByToken` quando o usuário não é encontrado com o token de acesso.
    
- **Não implementados**
- 
## utils
### Classe ParameterValidator
- **Implementados**
  1. **Função: validateUsername**
    - Verifica se uma exceção é lançada quando o nome de usuário é nulo;
    - Verifica se uma exceção é lançada quando o nome de usuário está vazio;
    - Verifica se uma exceção é lançada quando o nome de usuário é muito curto;
    - Verifica se uma exceção é lançada quando o nome de usuário é muito longo;
    - Verifica se uma exceção é lançada quando o nome de usuário tem formato inválido.

  2. **Função: validateEmail**
    - Verifica se uma exceção é lançada quando o endereço de e-mail é nulo;
    - Verifica se uma exceção é lançada quando o endereço de e-mail está vazio;
    - Verifica se uma exceção é lançada quando o endereço de e-mail é muito longo;
    - Verifica se uma exceção é lançada quando o endereço de e-mail tem formato inválido.

  3. **Função: validatePassword**
    - Verifica se uma exceção é lançada quando a senha é nula;
    - Verifica se uma exceção é lançada quando a senha é muito curta;
    - Verifica se uma exceção é lançada quando a senha é muito longa;
    - Verifica se uma exceção é lançada quando a senha não contém números;
    - Verifica se uma exceção é lançada quando a senha não contém caracteres especiais;
    - Verifica se uma exceção é lançada quando a senha não contém letras maiúsculas.
- **Não implementados**