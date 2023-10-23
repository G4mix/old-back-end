# Como rodar o back-end do Gamix

## Sumário
- [Como rodar o back-end do Gamix](#como-rodar-o-back-end-do-gamix)
  - [Sumário](#sumário)
  - [Introdução](#introdução)
  - [Rodando com o Docker](#rodando-com-o-docker)
    - [Passo 1: Instalação do Docker](#passo-1-instalação-do-docker)
    - [Passo 2: Executando o projeto com o Docker](#passo-2-executando-o-projeto-com-o-docker)
  - [Rodando no Eclipse](#rodando-no-eclipse)
    - [Passo 1: Instalação do Eclipse](#passo-1-instalação-do-eclipse)
    - [Passo 2: Configuração do Ambiente no Eclipse](#passo-2-configuração-do-ambiente-no-eclipse)
    - [Passo 3: Instalação do PostgreSQL](#passo-3-instalação-do-postgresql)
    - [Passo 4: Configuração da Conexão com o PostgreSQL](#passo-4-configuração-da-conexão-com-o-postgresql)
    - [Executando o Projeto no Eclipse](#executando-o-projeto-no-eclipse)
  - [Rodando no VSCode](#rodando-no-vscode)
    - [Passo 1: Instalação do VSCode](#passo-1-instalação-do-vscode)
    - [Passo 2: Configuração do Ambiente no VSCode](#passo-2-configuração-do-ambiente-no-vscode)
    - [Passo 3: Instalação do PostgreSQL](#passo-3-instalação-do-postgresql-1)
    - [Passo 4: Configuração da Conexão com o PostgreSQL](#passo-4-configuração-da-conexão-com-o-postgresql-1)
    - [Passo 5: Instalação do maven](#passo-5-instalação-do-maven)
    - [Executando o Projeto no VSCode](#executando-o-projeto-no-vscode)

## Introdução
Este guia fornece instruções detalhadas sobre como configurar seu ambiente de desenvolvimento em qualquer sistema operacional e no codespaces para rodar o back-end do Gamix.

## Rodando com o Docker

### Passo 1: Instalação do Docker
- Baixe e instale o Docker a partir do [site oficial](https://www.docker.com/products/docker-desktop/).
- Caso o seu computador não suporte o Docker Desktop instale o [docker toolbox no github](https://github.com/docker-archive/toolbox/releases).
- Siga as instruções de instalação padrão.

### Passo 2: Executando o projeto com o Docker
- Assumindo que você já instalou o Docker e ele está iniciado, abra um terminal que tenha permissões para executar o Docker, vá para o diretório raíz do back-end nesse terminal, o caminho pode ser algo parecido com isso: `C:\Users\User\Desktop\Lucas\gamix\back-end`.
- Digite `docker-compose up`.
- Agora basta esperar tudo iniciar e começar a programar utilizando algum editor de código-fonte como o VSCode ou o Eclipse, o Live Reload já está funcionando, basta programar! 

## Rodando no Eclipse

### Passo 1: Instalação do Eclipse
- Baixe e instale o Eclipse IDE para Java EE Developers a partir do [site oficial](https://www.eclipse.org/downloads/packages/release/2023-09/r).
- Siga as instruções de instalação padrão.

### Passo 2: Configuração do Ambiente no Eclipse
- Abra o Eclipse e crie um novo workspace.
- Importe o projeto Gamix como um projeto Maven existente.

### Passo 3: Instalação do PostgreSQL
- Faça o download da versão mais recente do PostgreSQL do [site oficial](https://www.postgresql.org/download/).
- Siga as instruções de instalação padrão.

### Passo 4: Configuração da Conexão com o PostgreSQL
- Abra o pgAdmin (fornecido durante a instalação do PostgreSQL) e se conecte ao banco de dados já existente com a senha usada durante a instalação.
- Configure as credenciais de acesso ao banco de dados no arquivo .env do projeto.

### Executando o Projeto no Eclipse
- Com tudo configurado você deve clicar com o botão direito na pasta principal do Gamix, clique em Maven > Update Project e pode rodar a opção force update.
- Clique com o botão direito no arquivo `src\main\java\com\gamix\GamixApplication.java, encontre a opção start e clique nela, rode como Java Application.

## Rodando no VSCode

### Passo 1: Instalação do VSCode
- Baixe e instale o VSCode a partir do [site oficial](https://code.visualstudio.com/download).
- Siga as instruções de instalação padrão.

### Passo 2: Configuração do Ambiente no VSCode
- Abra o VSCode.
- Abra a pasta do projeto Gamix.
- Baixe extensões que vão auxiliar ao programa localmente como o [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack).

### Passo 3: Instalação do PostgreSQL
- Faça o download da versão mais recente do PostgreSQL do [site oficial](https://www.postgresql.org/download/).
- Siga as instruções de instalação padrão.

### Passo 4: Configuração da Conexão com o PostgreSQL
- Abra o pgAdmin (fornecido durante a instalação do PostgreSQL) e se conecte ao banco de dados já existente com a senha usada durante a instalação.
- Configure as credenciais de acesso ao banco de dados no arquivo .env do projeto.

### Passo 5: Instalação do maven
- Baixe e instale o Maven a partir do [site oficial](https://maven.apache.org/download.cgi), escolha a versão que fizer mais sentido para o seu contexto.
- Siga as instruções de instalação padrão.

### Executando o Projeto no VSCode
- Abra um terminal no vscode, certifique-se de que o seu terminal está no diretório raíz do projeto back-end, exemplo de como pode estar: `C:\Users\User\Desktop\back-end>`, se você tiver aberto a pasta do projeto e um terminal é provável que já esteja no local correto.
- Execute digitando mvn spring-boot:run.

---

**Observação**: Certifique-se de ter realizado todos os passos com precisão para garantir que o projeto Gamix funcione corretamente no seu ambiente de desenvolvimento.