package br.com.selecao.resource;

import br.com.selecao.dto.PessoaDTO;
import br.com.selecao.model.Pessoa;
import br.com.selecao.model.Tarefa;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Classe de teste para todos os endpoints do PessoaResource.
 */
@QuarkusTest
public class PessoaResourceTest {

    /**
     * Este método é executado ANTES de CADA teste.
     * Sua função é limpar as tabelas para garantir que os testes
     * sejam independentes e não influenciem uns aos outros.
     */
    @BeforeEach
    @Transactional
    public void setup() {
        // Apaga todas as tarefas primeiro por causa da chave estrangeira
        Tarefa.deleteAll();
        // Depois apaga todas as pessoas
        Pessoa.deleteAll();
    }

    /**
     * Testa o endpoint POST /pessoas para garantir que uma nova pessoa pode ser
     * criada com sucesso.
     */
    @Test
    public void deveAdicionarPessoaComSucesso() {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.nome = "Joana d'Arc";
        pessoaDTO.departamento = "Estratégia";

        given()
            .contentType(ContentType.JSON)
            .body(pessoaDTO)
        .when()
            .post("/pessoas")
        .then()
            .statusCode(201)
            .body("nome", equalTo("Joana d'Arc"))
            .body("id", notNullValue());
    }

    /**
     * Testa o endpoint GET /pessoas/{id} para um ID que não existe.
     * Espera-se um status 404 Not Found.
     */
    @Test
    public void deveRetornarNotFoundAoBuscarPessoaInexistente() {
        given()
            .pathParam("id", 999) // Usando um ID que certamente não existe
        .when()
            .get("/pessoas/{id}")
        .then()
            .statusCode(404);
    }
    
    /**
     * Testa o endpoint PUT /pessoas/{id} para garantir a alteração de dados.
     */
    @Test
    public void deveAlterarPessoaComSucesso() {
        // Passo 1: Criar uma pessoa para ter um ID válido para alterar.
        PessoaDTO pessoaInicial = new PessoaDTO();
        pessoaInicial.nome = "Pessoa Original";
        pessoaInicial.departamento = "Antigo";

        Integer pessoaId = given()
            .contentType(ContentType.JSON)
            .body(pessoaInicial)
        .when()
            .post("/pessoas")
        .then()
            .statusCode(201)
            .extract().path("id"); // Extrai o ID da pessoa criada

        // Passo 2: Preparar o DTO com os novos dados.
        PessoaDTO pessoaAlterada = new PessoaDTO();
        pessoaAlterada.nome = "Pessoa Alterada";
        pessoaAlterada.departamento = "Novo";

        // Passo 3: Fazer a requisição PUT para alterar.
        given()
            .pathParam("id", pessoaId)
            .contentType(ContentType.JSON)
            .body(pessoaAlterada)
        .when()
            .put("/pessoas/{id}")
        .then()
            .statusCode(200)
            .body("nome", equalTo("Pessoa Alterada")) // Verifica se o nome foi alterado
            .body("departamento", equalTo("Novo"));    // Verifica se o departamento foi alterado
    }

    /**
     * Testa o endpoint DELETE /pessoas/{id} e verifica se a pessoa foi removida.
     */
    @Test
    public void deveRemoverPessoaComSucesso() {
        // Passo 1: Criar uma pessoa para ter um ID válido para remover.
        PessoaDTO pessoaParaRemover = new PessoaDTO();
        pessoaParaRemover.nome = "Pessoa Temporária";
        pessoaParaRemover.departamento = "Temporário";

        Integer pessoaId = given()
            .contentType(ContentType.JSON)
            .body(pessoaParaRemover)
        .when()
            .post("/pessoas")
        .then()
            .statusCode(201)
            .extract().path("id");

        // Passo 2: Fazer a requisição DELETE.
        given()
            .pathParam("id", pessoaId)
        .when()
            .delete("/pessoas/{id}")
        .then()
            .statusCode(204); // Verifica se o status é 204 No Content

        // Passo 3 (Verificação extra): Tentar buscar a pessoa removida.
        given()
            .pathParam("id", pessoaId)
        .when()
            .get("/pessoas/{id}")
        .then()
            .statusCode(404); // Espera-se 404 Not Found
    }
}