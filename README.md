# API de Gerenciamento de Tarefas

Este projeto é uma API REST para gerenciar tarefas, pessoas e departamentos, desenvolvida com [Quarkus](https://quarkus.io/), o Framework Java Supersônico e Subatômico.

---

## ⚙️ Configuração do Ambiente

Siga os passos abaixo para configurar o ambiente e executar o projeto.

### Pré-requisitos

- **JDK 17** (ou superior)
- **Maven 3.8** (ou superior)
- **PostgreSQL 14** (ou superior) em execução na porta padrão `5432`

---

## 🗄️ Criação do Banco de Dados

Antes de executar a aplicação pela primeira vez, crie o banco de dados:

1. Conecte-se ao seu servidor PostgreSQL local (usando o pgAdmin ou outro cliente).
2. Execute o comando SQL abaixo para criar o banco de dados:

```sql
CREATE DATABASE task_manager_db;
```

---

## 🚀 Executando a Aplicação

Com o banco de dados criado, execute o seguinte comando na raiz do projeto para iniciar a aplicação em modo de desenvolvimento (com live coding):

```bash
./mvnw quarkus:dev
```
> No Windows, use `mvnw.cmd quarkus:dev` se não estiver usando o Bash.

A aplicação estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## 🧪 Executando os Testes Unitários

Para rodar todos os testes automatizados do projeto, utilize o comando abaixo na raiz do projeto:

```bash
./mvnw test
```
> No Windows, use `mvnw.cmd test` se não estiver usando o Bash.

Os relatórios dos testes serão exibidos no terminal após a execução.

---

## 📚 Mais Informações

- [Documentação do Quarkus](https://quarkus.io/documentation/)
- [Documentação do Maven](https://maven.apache.org/guides/)
- [Documentação do PostgreSQL](https://www.postgresql.org/docs/)

---
