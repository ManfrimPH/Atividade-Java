# Sistema de E-commerce - Registro de Vendas

Este é um sistema de e-commerce simples em linha de comando que permite registrar vendas de produtos para usuários cadastrados.

## Funcionalidades

- Busca de usuário por e-mail
- Seleção de produtos por ID
- Processamento de pagamento com diferentes métodos (Cartão, Boleto, PIX)
- Registro de vendas no banco de dados
- Exibição de resumo da venda

## Requisitos

- Java 8 ou superior
- Maven
- SQLite

## Configuração

1. Clone o repositório
2. Execute o script SQL para criar as tabelas:
   ```bash
   sqlite3 database.sqlite < scripts/create_tables.sql
   ```

## Execução

Para executar o projeto, use o Maven:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.unicesumar.Main"
```

## Estrutura do Projeto

O projeto utiliza os padrões de projeto Strategy e Factory para o processamento de pagamentos:

- `PaymentStrategy`: Interface que define o contrato para as estratégias de pagamento
- `CreditCardPayment`, `BoletoPayment`, `PixPayment`: Implementações concretas das estratégias
- `PaymentFactory`: Fábrica para criar as estratégias de pagamento

## Banco de Dados

O sistema utiliza SQLite como banco de dados. As tabelas são:

- `usuarios`: Armazena os dados dos usuários
- `produtos`: Catálogo de produtos
- `vendas`: Registro das vendas realizadas
- `itens_venda`: Itens incluídos em cada venda

## Exemplo de Uso

1. Execute o programa
2. Digite o e-mail do usuário (ex: joao@example.com)
3. Digite os IDs dos produtos desejados (ex: 1,3)
4. Escolha a forma de pagamento (1-Cartão, 2-Boleto, 3-PIX)
5. Confirme a venda

# 🚀 Guia Rápido para Rodar o Projeto

## ✅ Pré-requisitos

As seguintes ferramentas devem estar instaladas:

- Java (JDK 17, por exemplo)
- Maven

---

## ☕ 1. Compilar e Executar o Projeto Java com Maven

### Navegue até a raiz do projeto (onde está o `pom.xml`) utilizando um terminal com `Prompt de Comando` ou `Windows Terminal`:

```bash
cd caminho/do/projeto
```

### Compilar o projeto:

```bash
mvn clean install
```

### Executar a aplicação:

```bash
mvn exec:java
```

> Caso o `pom.xml` não tenha o plugin `exec`, você pode executar manualmente com:

```bash
java -cp target/seu-jar-gerado.jar caminho.da.sua.Main
```

---
