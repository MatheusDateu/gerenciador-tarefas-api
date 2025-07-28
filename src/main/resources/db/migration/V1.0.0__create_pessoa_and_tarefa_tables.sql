-- Cria a tabela 'Pessoa' para armazenar as informações das pessoas.
CREATE TABLE Pessoa (
    id BIGINT NOT NULL PRIMARY KEY,
    nome VARCHAR(255),
    departamento VARCHAR(100)
);

-- Sequence para a geração de IDs da tabela Pessoa.
CREATE SEQUENCE Pessoa_SEQ
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

-- Cria a tabela 'Tarefa' para armazenar os detalhes das tarefas.
CREATE TABLE Tarefa (
    id BIGINT NOT NULL PRIMARY KEY,
    titulo VARCHAR(255),
    descricao TEXT,
    prazo DATE,
    departamento VARCHAR(100),
    duracao INT,
    finalizado BOOLEAN NOT NULL,
    pessoaAlocada_id BIGINT,
    CONSTRAINT fk_pessoa_alocada
        FOREIGN KEY(pessoaAlocada_id) 
        REFERENCES Pessoa(id)
);

-- Sequence para a geração de IDs da tabela Tarefa.
CREATE SEQUENCE Tarefa_SEQ
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;