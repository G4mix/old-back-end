# Como rodar o back-end do Gamix

## Sumário
- [Como rodar o back-end do Gamix](#como-rodar-o-back-end-do-gamix)
  - [Sumário](#sumário)
  - [Introdução](#introdução)
  - [Rodando com o Docker](#rodando-com-o-docker)
    - [Passo 1: Instalação do Docker](#passo-1-instalação-do-docker)
    - [Passo 2: Executando o projeto com o Docker](#passo-2-executando-o-projeto-com-o-docker)
  - [Rodando no codespaces](#rodando-no-codespaces)
    - [Usando o docker](#usando-o-docker)
      - [Passo 1: Iniciando o codespaces](#passo-1-iniciando-o-codespaces)
      - [Passo 2: Rodando com o docker](#passo-2-rodando-com-o-docker)

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

## Rodando no codespaces
### Usando o docker
#### Passo 1: Iniciando o codespaces
- Crie um codespaces na branch e no repositório que desejar usando o [github codespaces](https://github.com/codespaces)
- No nosso caso inicie um codespaces no repositório G4mix/back-end e pode selecionar a branch que desejar
#### Passo 2: Rodando com o docker
- Basta digitar `docker-compose up` e o back-end irá subir, o codespaces já vem com o docker instalado.

---

**Observação**: Certifique-se de ter realizado todos os passos com precisão para garantir que o projeto Gamix funcione corretamente no seu ambiente de desenvolvimento.