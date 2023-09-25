# Guia de Instalação para Execução no Eclipse (Windows)

## Sumário
- [Guia de Instalação para Execução no Eclipse (Windows)](#guia-de-instalação-para-execução-no-eclipse-windows)
  - [Sumário](#sumário)
  - [Introdução](#introdução)
  - [Assistência em Vídeo](#assistência-em-vídeo)
  - [Passo 1: Instalação do Eclipse](#passo-1-instalação-do-eclipse)
  - [Passo 2: Configuração do Ambiente no Eclipse](#passo-2-configuração-do-ambiente-no-eclipse)
  - [Passo 3: Instalação do PostgreSQL](#passo-3-instalação-do-postgresql)
  - [Passo 4: Configuração da Conexão com o PostgreSQL](#passo-4-configuração-da-conexão-com-o-postgresql)
  - [Executando o Projeto no Eclipse](#executando-o-projeto-no-eclipse)

## Introdução
Este guia fornece instruções detalhadas sobre como configurar seu ambiente de desenvolvimento no Windows para rodar o projeto Gamix no Eclipse, incluindo a instalação do PostgreSQL.

## Assistência em Vídeo
Para uma demonstração visual desses passos, assista ao [vídeo de instalação completo](https://www.youtube.com/watch?v=7hR0zw5XFwQ) para configurar o ambiente no Windows e executar o projeto Gamix.

## Passo 1: Instalação do Eclipse
- Baixe e instale o Eclipse IDE para Java EE Developers a partir do [site oficial](https://www.eclipse.org/downloads/packages/release/2023-09/r).
- Siga as instruções de instalação padrão.

## Passo 2: Configuração do Ambiente no Eclipse
- Abra o Eclipse e crie um novo workspace.
- Importe o projeto Gamix.

## Passo 3: Instalação do PostgreSQL
- Faça o download da versão mais recente do PostgreSQL do [site oficial](https://www.postgresql.org/download/).
- Siga as instruções de instalação padrão.

## Passo 4: Configuração da Conexão com o PostgreSQL
- Abra o pgAdmin (fornecido durante a instalação do PostgreSQL) e se conecte ao banco de dados já existente com a senha usada durante a instalação.
- Configure as credenciais de acesso ao banco de dados no arquivo .env do projeto.

## Executando o Projeto no Eclipse
- Certifique-se de ter o Tomcat configurado no Eclipse.
- Inicie o servidor Tomcat e execute o projeto.

---

**Observação**: Certifique-se de ter realizado todos os passos com precisão para garantir que o projeto Gamix funcione corretamente no seu ambiente de desenvolvimento.