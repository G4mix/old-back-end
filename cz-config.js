module.exports = {
  "types": [
    {
      "value": "chore",
      "name": "chore: Atualização de tarefas que não afetam o código de produção, como mudanças de ferramentas, configurações e bibliotecas."
    },
    {
      "value": "feat",
      "name": "feat: Adição de novas funcionalidades ou implantações ao código."
    },
    {
      "value": "fix",
      "name": "fix: Correção de bugs ou erros no código."
    },
    {
      "value": "refactor",
      "name": "refactor: Mudanças no código que não afetam a funcionalidade final, como refatorações."
    },
    {
      "value": "docs",
      "name": "docs: Alterações em arquivos de documentação."
    },
    {
      "value": "perf",
      "name": "perf: Alterações de código que melhoram o desempenho."
    },
    {
      "value": "style",
      "name": "style: Alterações de formatação que não afetam o significado do código."
    },
    {
      "value": "test",
      "name": "test: Adição ou correção de testes automatizados."
    },
    {
      "value": "build",
      "name": "build: Mudanças que afetam o sistema de construção ou dependências externas."
    },
    {
      "value": "ci",
      "name": "ci: Alterações em arquivos de configuração de integração contínua."
    },
    {
      "value": "env",
      "name": "env: Modificações em arquivos de configuração relacionados à integração contínua."
    },
    {
      "value": "fit",
      "name": "fit: Pequenos ajustes no código."
    }
  ],
  messages: {
    type: "Selecione o tipo de alteração que você fez:",
    scope: "\nIndique o ESCOPO desta mudança (opcional):",
    subject: "Escreva uma breve descrição da mudança:\n",
    body: "Forneça uma descrição mais longa da mudança (opcional). Use "|" para quebrar novas linhas:\n",
    breaking: "Liste quaisquer mudanças que quebraram outros códigos existentes (optional):\n",
    footer: "Liste quaisquer issues fechados por esta alteração (opcional). Ex.: #31, #34:\n",
    confirmCommit: "Tem certeza de que deseja prosseguir com o commit acima?",
  },
}