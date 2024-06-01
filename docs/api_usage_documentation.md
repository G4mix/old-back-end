# API Usage Documentation

## Sumário

- [API Usage Documentation](#api-usage-documentation)
  - [Sumário](#sumário)
  - [1. Introdução](#1-introdução)
    - [1.1 Atenção!](#11-atenção)
  - [2. Autenticação](#2-autenticação)
    - [2.1 SignUp (Cadastro)](#21-signup-cadastro)
    - [2.2 SignIn (Login)](#22-signin-login)
  - [4. Usuários](#4-usuários)
    - [4.1 Encontrar Usuário por Nome de Usuário](#41-encontrar-usuário-por-nome-de-usuário)
    - [4.2 Encontrar Usuário por E-mail](#42-encontrar-usuário-por-e-mail)
    - [4.3 Excluir Conta](#43-excluir-conta)
  - [5. Posts](#5-posts)
    - [5.1 Criar Postagem](#51-criar-postagem)
    - [5.2 Listar posts](#52-listar-posts)
    - [5.3 Encontrar post por Id](#53-encontrar-post-por-id)
    - [5.4 Encontrar post por título](#54-encontrar-post-por-título)
    - [5.5 Atualizar postagem](#55-atualizar-postagem)
    - [5.6 Excluir postagem](#56-excluir-postagem)
  - [Contato](#contato)

---

## 1. Introdução

Este documento descreve os endpoints disponíveis na API do sistema, juntamente com os métodos HTTP permitidos e os detalhes de autenticação e parâmetros necessários para cada um deles.

### 1.1 Atenção!

Existem métodos que precisam do header Authorization: "Bearer accessToken", fique atento!
Esse Header é retornado em todos os métodos uma vez que está logado, caso faltem 20 minutos para expirar o token, o novo token é retornado no header.
![Header demo](images/imnsonia_header_demonstration.png)
![Header demo 2](images/imnsonia_header_demonstration2.png)
![Header demo 3](images/imnsonia_header_demonstration3.png)
![Header demo 4](images/imnsonia_header_demonstration4.png)

## 2. Autenticação

### 2.1 SignUp (Cadastro)

- **Endpoint**: `/auth/signup`
- **Método**: `POST`
- **Descrição**: Cria um novo usuário com as credenciais fornecidas.
- **Parâmetros**:
  - `username` (String, obrigatório): Nome de usuário do novo usuário.
  - `email` (String, obrigatório): Endereço de e-mail do novo usuário.
  - `password` (String, obrigatório): Senha do novo usuário.
- **Retorno**:
  - `Header "Authorization: Bearer accessToken"` (String): Token de acesso JWT.

```json
{
  "username": "example_user",
  "email": "example@gmail.com",
  "password": "Example_password123!"
}
```

### 2.2 SignIn (Login)

- **Endpoint**: `/auth/signin`
- **Método**: `POST`
- **Descrição**: Autentica um usuário com as credenciais fornecidas, e-mail e senha ou username e senha.
- **Parâmetros**:
  - `username` (String): Nome de usuário do usuário.
  - `email` (String): Endereço de e-mail do usuário.
  - `password` (String, obrigatório): Senha do usuário.
  - `rememberMe` (Boolean, obrigatório): Define se o usuário deseja permanecer logado.
- **Retorno**:
  - `Header "Authorization: Bearer accessToken"` (String): Token de acesso JWT.

```json
{
  "username": "example_user",
  "password": "Example_password123!",
  "rememberMe": true
}
```
  
## 3. Usuários

### 3.1 Encontrar Usuário por Nome de Usuário

- **Endpoint**: `/users/:username`
- **Método**: `GET`
- **Descrição**: Encontra um usuário pelo nome de usuário.
- **Parâmetros**:
  - `username` (String, obrigatório): Nome de usuário do usuário.
- **Retorno**:
  - `user` (Objeto User): Usuário encontrado.

```json
{
  "username": "example_user"
}
```

### 3.2 Encontrar Usuário por E-mail

- **Endpoint**: `/users/:email`
- **Descrição**: Encontra um usuário pelo endereço de e-mail.
- **Parâmetros**:
  - `email` (String, obrigatório): Endereço de e-mail do usuário.
- **Retorno**:
  - `user` (Objeto User): Usuário encontrado.

```json
{
  "email": "example_user@email.com"
}
```

### 3.3 Excluir Conta por Id

**Endpoint**: `/users/:id`
- **Descrição**: Exclui a conta de um usuário pelo seu Id.
- **Retorno**:
  - `success` (Boolean): Indica se a conta foi excluída com sucesso.

```json
{
  "sucess": true
}
```

## 4. Posts

### 4.1 Criar Postagem

- **Endpoint**: `/posts`
- **Método**: `POST`
- **Descrição**: Cria uma nova postagem.
- **Parâmetros**: 
  - `Objeto PartialPostInput`
    - `authorId` (Int, obrigatório) - ID do usuário autor da postagem
    - `title` (String, obrigatório) - Título da postagem
    - `content` (String, obrigatório) - Conteúdo da postagem
- **Retorno**:
  - `success` (Boolean): Indica se a postagem foi criada com sucesso
  - `post` (Objeto Post): Post criado
 
```json
{
  "success": true,
  "post": {
    "authorId": 1,
    "title": "Minha primeira postagem",
    "content": "Conteúdo da minha primeira postagem"
  }
}
```

### 4.2 Listar posts

- **Endpoint**: `/posts`
- **Método**: `GET`
- **Descrição**: Lista todas postagens.
- **Parâmetros**:
  - `skip` (Int, opcional): Número de registros a serem ignorados.
  - `limit` (Int, opcional): Número máximo de registros a serem retornados.
- **Retorno**:
  - List<Post> - lista de postagens cadastradas.
 
```json
[
  {
    "id": 1,
    "author": {
      "id": 1,
      "username": "Gabriel",
      "email": "gabriel.vicente3@fatec.sp.gov.br"
    },
    "title": "Postagem 1",
    "content": "Conteúdo da postagem 1"
  },
  {
    "id": 2,
    "author": {
      "id": 2,
      "username": "João",
      "email": "joao1@email.com"
    },
    "title": "Postagem 2",
    "content": "Conteúdo da postagem 2"
  },
    {
    "id": 3,
    "author": {
      "id": 1,
      "username": "Gabriel",
      "email": "gabriel.vicente3@fatec.sp.gov.br"
    },
    "title": "Postagem 3",
    "content": "Conteúdo da postagem 3"
  }
]
```

### 4.3 Encontrar post por Id

- **Endpoint**: `/posts/:id`
- **Método**: `GET`
- **Descrição**: Retorna a postagem de acordo com o Id fornecido.
- **Parâmetros**:
  - `id` (Int): Id do post a ser recuperado.
- **Retorno**:
  - `post` (Objeto Post): Post recuperado.
 
```json
{
  "id": 1,
  "author": {
    "id": 1,
    "username": "Gabriel",
    "email": "gabriel.vicente3@fatec.sp.gov.br"
  },
  "title": "Postagem 1",
  "content": "Conteúdo da postagem 1"
}
```

### 4.4 Encontrar post por título

- **Endpoint**: `/post/:title`
- **Método**: ``GET
- **Descrição**: Retorna a postagem de acordo com o título fornecido.
- **Parâmetros**:
  - `title` (String): Título do post a ser recuperado.
- **Retorno**:
  - `post` (Objeto Post): Post recuperado.
 
```json
{
  "id": 1,
  "author": {
    "id": 1,
    "username": "Gabriel",
    "email": "gabriel.vicente3@fatec.sp.gov.br"
  },
  "title": "Título da postagem 1",
  "content": "Conteúdo da postagem 1"
}
```

### 4.5 Atualizar postagem

- **Endpoint**: `/post/update/:postId`
- **Método**: `PATCH`
- **Descrição**: Edita uma postagem já criada, referenciando seu Id.
- **Parâmetros de rota**:
  - `postId` (Int, obrigatório): Id do post a ser atualizado.
- **Corpo da requisição**:
  - `input`: (Objeto PartialPostInput): Indica os campos e valores que deverão ser atualizados. 
- **Retorno**:
  - `success` (Boolean): Retorna mensagem de sucesso ou erro.
  - `newPost` (Objeto Post): Retorna o novo post.

- **Corpo da requisição **:

```json
{
  "title": "Postagem 1: UPDATE",
  "content": "Informações adicionais para postagem 1"
}
```
 
```json
{
  "sucess": true,
  "newPost": {
    "id": 1,
    "author": {
      "id": 1,
      "username": "Gabriel",
      "email": "gabriel.vicente3@fatec.sp.gov.br"
    },
  "title": "Postagem 1: UPDATE",
  "content": "Informações adicionais para postagem 1"
  }
}
```

### 4.6 Excluir postagem

- **Endpoint**: `/posts/delete/:postId`
- **Descrição**: Busca e deleta uma postagem com base no Id fornecido
- **Parâmetros**:
  - `postId` (Int, obrigatório): Id do post a ser excluído.
- **Retorno**:
  - `success` (Boolean): Indica se a exclusão foi feita corretamente.
 
```json
{
  "success": true
}
```

## Contato

Para dúvidas ou sugestões relacionadas ao Back-end Gamix, entre em contato com o time de desenvolvimento responsável:

- [Gabriel Vicente - Sênior Back-end](https://github.com/gabrielOliv1)
- [Lucas Christian - Engenheiro de Software](https://github.com/Lucas-Christian)

Agradecemos por ser parte da comunidade Gamix e por contribuir para a construção de projetos de jogos emocionantes!

---

© 2023 Gamix. Todos os direitos reservados.
