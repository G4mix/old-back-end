# Histórico de Testes

Este arquivo registra uma visão geral dos testes realizados em diferentes classes do back-end do Gamix.

## Testes de Validação de Parâmetros
- **Implementados**
  - **Teste: validateUsername**
    - Verifica se uma exceção é lançada quando o nome de usuário é nulo;
    - Verifica se uma exceção é lançada quando o nome de usuário está vazio;
    - Verifica se uma exceção é lançada quando o nome de usuário é muito curto;
    - Verifica se uma exceção é lançada quando o nome de usuário é muito longo;
    - Verifica se uma exceção é lançada quando o nome de usuário tem formato inválido.

  - **Teste: validateEmail**
    - Verifica se uma exceção é lançada quando o endereço de e-mail é nulo;
    - Verifica se uma exceção é lançada quando o endereço de e-mail está vazio;
    - Verifica se uma exceção é lançada quando o endereço de e-mail é muito longo;
    - Verifica se uma exceção é lançada quando o endereço de e-mail tem formato inválido.

  - **Teste: validatePassword**
    - Verifica se uma exceção é lançada quando a senha é nula;
    - Verifica se uma exceção é lançada quando a senha é muito curta;
    - Verifica se uma exceção é lançada quando a senha é muito longa;
    - Verifica se uma exceção é lançada quando a senha não contém números;
    - Verifica se uma exceção é lançada quando a senha não contém caracteres especiais;
    - Verifica se uma exceção é lançada quando a senha não contém letras maiúsculas.
- **Não implementados**

## Testes da Classe PasswordUserService
- **Implementados**
  - **Teste: createPasswordUser**
    - Verifica se a função `createPasswordUser` cria um novo usuário com senha e retorna o objeto `PasswordUser` criado.
    
  - **Teste: refreshToken**
    - Usa a função `refreshToken` com um refreshToken válido, e é esperado o retorno de novos jwtTokens.
    - Usa a função `refreshToken` com um refreshToken inválido, e lança a exceção `InvalidRefreshToken`.

  - **Teste: signInPasswordUser**
    - Usa a função `signInPasswordUser` com credenciais válidas usando o username, e é esperado o retorno de novos jwtTokens;
    - Usa a função `signInPasswordUser` com credenciais válidas usando o email, e é esperado o retorno de novos jwtTokens;
    - Usa a função `signInPasswordUser` usando um email que não existe, e é esperado uma exceção `UserNotFound`;
    - Usa a função `signInPasswordUser` usando um email que existe, mas não é um passwordUser, é esperado uma exceção `PasswordUserNotFound`;
    - Usa a função `signInPasswordUser` usando um email que existe, mas erra a senha lançando um erro `PasswordWrong`;
    - Usa a função `signInPasswordUser` usando um email que existe, mas erra a senha lançando um erro `PasswordWrong`, tenta novamente, e é bloqueado por tentativas excessivas lançando um erro `ExcessiveFailedLoginAttempts`, e após o tempo passar é desbanido;
    - Usa a função `signInPasswordUser` com credenciais válidas usando o email, mas de alguma forma os tokens JWT acabam sendo nulos, e é lançada uma exceção `NullJwtTokens`.
  
  - **Teste: signOutPasswordUser**
    - Usa a função `signOutPasswordUser` 

  - **Teste: signUpPasswordUser**
    - Usa a função `signUpPasswordUser` 
- **Não implementados**

## Testes da Classe UserService
- **Implementados**
  - **Teste: Criar Usuário com Sucesso**
    - Verifica se a função `createUser` cria um novo usuário com sucesso e retorna o objeto `User` criado.

  - **Teste: Deletar Conta com Sucesso**
    - Verifica se a função `deleteAccount` deleta a conta do usuário com sucesso.

  - **Teste: Deletar Conta com Token de Acesso Inválido**
    - Verifica se a função `deleteAccount` lança uma exceção quando o token de acesso é inválido.

  - **Teste: Encontrar Todos os Usuários**
    - Verifica se a função `findAllUsers` retorna uma lista de usuários corretamente.

  - **Teste: Encontrar Usuário por E-mail**
    - Verifica se a função `findUserByEmail` retorna o usuário correto a partir do e-mail.

  - **Teste: Encontrar Usuário por Token**
    - Verifica se a função `findUserByToken` retorna o usuário correto a partir do token de acesso.

  - **Teste: Encontrar Usuário por Nome de Usuário**
    - Verifica se a função `findUserByUsername` retorna o usuário correto a partir do nome de usuário.

  - **Teste: Atualizar Usuário com Sucesso**
    - Verifica se a função `updateUser` atualiza os dados do usuário com sucesso.

  - **Teste: Atualizar Usuário com Token Inválido**
    - Verifica se a função `updateUser` lança uma exceção quando o token de acesso é inválido.

  - **Teste: Atualizar Usuário com Exceção Lançada**
    - Verifica se a função `updateUser` lança a exceção correta quando ocorre um erro ao atualizar o usuário.
- **Não implementados**
