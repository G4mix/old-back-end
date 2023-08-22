# Guia de Estilo de Código - Back-end Gamix (Java)

Neste projeto, seguimos um guia de estilo de código para manter a consistência e a legibilidade em todo o código Back-end. Essas diretrizes ajudarão a garantir que todos os desenvolvedores possam colaborar efetivamente e compreender facilmente o código.

## Formatação de Código

- Use indentação com 4 espaços para cada nível de recuo.
- Evite linhas de código com mais de 80 caracteres.
- Use linhas em branco para separar blocos lógicos de código.
- Use espaços em branco para separar operadores e expressões.
- Evite o uso de espaços em branco em excesso.

## Nomes de Variáveis e Funções

- Use nomes descritivos e significativos para variáveis e funções.
- Utilize camelCase para nomes de variáveis e funções (por exemplo, `nomeDaVariavel`, `calcularTotal`).

## Comentários

- Use comentários para explicar o código complexo ou partes que possam ser confusas para outros desenvolvedores.
- Mantenha os comentários atualizados e relevantes. Comentários desatualizados podem levar a confusões.

## Importações

- Mantenha as importações organizadas e agrupadas por pacote.
- Evite importar classes que não são usadas no código.

## Estrutura de Diretórios

A estrutura de diretórios a seguir é comum em projetos Java que utilizam o framework Spring Boot. Ela ajuda a organizar o código-fonte de maneira lógica e modular.

- `src/`: O diretório de origem (source) do projeto, onde fica o código-fonte.

- `main/`: Contém o código de produção do aplicativo.

- `java/`: Armazena o código Java da aplicação.

- `com/`: Prefixo de pacote para evitar conflitos de nome. Indica uma organização, como "com.gamix".

- `gamix/`: Pacote da aplicação, onde você coloca as classes relacionadas.

Dentro do pacote `com.gamix`, a estrutura padrão do projeto Spring Boot inclui:

- `controller/`: Classes que recebem requisições HTTP e direcionam para operações no serviço.

- `service/`: Lógica de negócios da aplicação, interagindo com repositórios.

- `repository/`: Interfaces ou classes que definem como acessar dados no banco de dados.

- `model/`: Classes que representam entidades do domínio, mapeando para tabelas no banco de dados.

## Tratamento de Exceções

- Sempre trate exceções de forma adequada e forneça mensagens de erro úteis para facilitar a depuração.
- Evite capturar exceções genéricas, especifique exceções específicas sempre que possível.

## Exemplo de Código

```java
package com.gamix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @GetMapping("/games")
    public List<Game> getGames() {
        List<Game> games = gameService.getAllGames();
        return games;
    }
}
```

## Contato

Se você tiver dúvidas ou precisar de assistência com as diretrizes de estilo de código, entre em contato com o time de desenvolvimento responsável pelo Back-end:

- [Gabriel Vicente - Sênior Back-end](https://github.com/gabrielOliv1)
- [Lucas Christian - Engenheiro de Software](https://github.com/Lucas-Christian)

Obrigado por contribuir para a plataforma Gamix e ajudar a criar jogos incríveis para nossa comunidade!

---

© 2023 Gamix. Todos os direitos reservados.
