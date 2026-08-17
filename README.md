# 🏦 NexusLedger

> **Core Engine para Processamento Financeiro e Gestão de Contas Bancárias.**
O nome **NexusLedger** reflete a essência da arquitetura do sistema:
* **Nexus (Conexão/Centro):** Representa a camada de serviço (`BancoService`) que atua como um hub central, orquestrando as transferências interbancárias e conectando contas de forma segura.
* **Ledger (Livro-Razão):** Reflete o rigor com a auditoria. O histórico de transações do sistema foi desenhado para ser blindado e imutável, assim como um autêntico livro de registros financeiros.

O **NexusLedger** é um motor transacional (Core Banking MVP) desenvolvido em Java. O projeto foi construído do zero, sem o uso de frameworks engessados, com o objetivo de demonstrar a aplicação profunda e rigorosa de **Programação Orientada a Objetos (POO)**, **Princípios SOLID**, Estruturas de Dados e testes automatizados.

📄 **Leia a documentação arquitetural completa:** [Documentacao_Tecnica.pdf](./docs/Documentacao_Tecnica.pdf)

---

## 🚀 Principais Funcionalidades

* **Gestão de Contas:** Suporte para contas com naturezas distintas (Corrente com limite de Cheque Especial e Poupança com rendimentos).
* **Transações Financeiras:** Depósitos e Saques com validação matemática de saldo.
* **Orquestração de PIX:** Transferências atômicas com travas de segurança (bloqueio de Auto-PIX e validação de existência).
* **Auditoria de Extrato:** Registro imutável de todas as transações, com geração de descrições dinâmicas de acordo com o limite utilizado.

---

## 🧱 Aplicações Práticas do SOLID

O design do projeto foi estritamente guiado pelos princípios SOLID para garantir um baixo acoplamento e alta coesão:

* **[S] Single Responsibility Principle (SRP):** Responsabilidades estritamente separadas. A classe `Transacao` apenas transporta dados (DTO); as classes de domínio (`ContaCorrente`, `ContaPoupanca`) validam apenas regras matemáticas do seu próprio saldo; e o `BancoService` lida unicamente com a orquestração entre contas.
* **[O] Open/Closed Principle (OCP):** O motor de cálculo de tributos foi construído para ser fechado para modificação e aberto para extensão. É possível criar um novo tipo de conta (ex: `ContaSalario`) com novas regras de tributação sem precisar alterar nenhuma linha de código da superclasse ou do serviço central.
* **[L] Liskov Substitution Principle (LSP):** O sistema garante que as classes filhas (`ContaCorrente` e `ContaPoupanca`) possam substituir a classe mãe (`ContaBancaria`) sem quebrar a aplicação. Ambas são manipuladas genericamente dentro da lista do `BancoService` sem causar efeitos colaterais.
* **[D] Dependency Inversion Principle (DIP):** A camada de orquestração (`BancoService`) não depende de implementações concretas (não trabalha olhando se é Corrente ou Poupança), mas sim de abstrações. Ela manipula a entidade abstrata `ContaBancaria`, mantendo o código flexível.

---

## 🧠 Destaques Técnicos & Arquitetura

Além do SOLID, o projeto serve como um laboratório prático de Engenharia de Software:

* **Encapsulamento Defensivo:** Uso da `Collections.unmodifiableList` para impedir que o extrato financeiro seja fraudado ou apagado na memória.
* **Polimorfismo:** Eliminação de lógicas condicionais (`if/else`) pesadas no cálculo de tributos, delegando o comportamento para as instâncias em tempo de execução.
* **DTOs Imutáveis:** Uso de Java `records` e `UUID` nativo para garantir que uma `Transacao` jamais seja adulterada após registrada.
* **Princípio Fail-Fast:** Arquitetura orientada a Exceções Customizadas (`SaldoInsuficienteException`, `ValorInvalidoException`), quebrando o fluxo de execução imediatamente ao detectar violações de negócio.
* **Design Pattern (Facade):** A camada `BancoService` atua como uma Fachada, escondendo toda a complexidade do domínio do usuário final durante uma transferência.

---

## 🛠️ Stack Tecnológica

* **Linguagem:** Java 17+
* **Gerenciador de Dependências:** Maven
* **Testes e Qualidade:** JUnit 5 (Testes Unitários focados em BDD)
* **Modelagem:** Astah UML (Casos de Uso, Classes e Atividades)

---

👨‍💻 Desenvolvido por [Luís Gustavo](https://github.com/luisgustavos29)
