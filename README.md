API de Gerenciamento de Tarefas
Este projeto é uma API REST para gerenciar tarefas, pessoas e departamentos, desenvolvido com Quarkus, o Framework Java Supersônico e Subatômico.

Para saber mais sobre o Quarkus, visite o site oficial: https://quarkus.io/.

⚙️ Configuração do Ambiente
Siga os passos abaixo para configurar o ambiente e executar o projeto.

Pré-requisitos
JDK 17 (ou superior)

Maven 3.8 (ou superior)

PostgreSQL 14 (ou superior) em execução na porta 5432.

Criação do Banco de Dados
Antes de executar a aplicação pela primeira vez, é necessário criar o banco de dados que será utilizado.

Conecte-se ao seu servidor PostgreSQL local (usando o pgAdmin ou outro cliente).

Execute o seguinte comando SQL para criar o banco de dados:
```SQL
CREATE DATABASE task_manager_db;
```

🚀 Executando a Aplicação
Modo de Desenvolvimento
Com o banco de dados criado, execute o seguinte comando na raiz do projeto para iniciar a aplicação em modo de desenvolvimento, que habilita o live coding: