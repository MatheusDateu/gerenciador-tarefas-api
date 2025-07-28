package br.com.selecao.resource;

import br.com.selecao.dto.PessoaDTO;
import br.com.selecao.dto.TarefaDTO;
import br.com.selecao.model.Pessoa;
import br.com.selecao.model.Tarefa;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * Classe de teste para os endpoints do TarefaResource.
 */
@QuarkusTest
public class TarefaResourceTest {

    /**
     * Limpa o banco de dados antes de cada teste para garantir a independência.
     */
    @BeforeEach
    @Transactional
    public void setup() {
        Tarefa.deleteAll();
        Pessoa.deleteAll();
    }

    /**
     * Testa a criação de uma tarefa alocada a uma pessoa existente.
     */
    @Test
    public void deveAdicionarTarefaComSucesso() {
        // Pre-condição: Criar uma pessoa para podermos alocar a tarefa.
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.nome = "Pessoa de Teste";
        pessoaDTO.departamento = "Qualidade";
        Integer pessoaId = given().contentType(ContentType.JSON).body(pessoaDTO).post("/pessoas").then().extract().path("id");

        // Preparação do DTO da tarefa
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.titulo = "Revisar código";
        tarefaDTO.descricao = "Revisar o código do novo módulo de pagamentos.";
        tarefaDTO.prazo = LocalDate.now().plusDays(5);
        tarefaDTO.departamento = "Qualidade";
        tarefaDTO.duracao = 4;
        tarefaDTO.pessoaAlocadaId = Long.valueOf(pessoaId);

        given()
            .contentType(ContentType.JSON)
            .body(tarefaDTO)
        .when()
            .post("/tarefas")
        .then()
            .statusCode(201)
            .body("titulo", equalTo("Revisar código"))
            .body("pessoaAlocada.id", equalTo(pessoaId));
    }

    /**
     * Testa a tentativa de criar uma tarefa alocada a uma pessoa que não existe.
     * Espera-se um erro 404 Not Found.
     */
    @Test
    public void naoDeveAdicionarTarefaParaPessoaInexistente() {
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.titulo = "Tarefa de Teste";
        tarefaDTO.pessoaAlocadaId = 999L; // ID que não existe

        given()
            .contentType(ContentType.JSON)
            .body(tarefaDTO)
        .when()
            .post("/tarefas")
        .then()
            .statusCode(404);
    }

    /**
     * Testa a finalização de uma tarefa.
     */
    @Test
    public void deveFinalizarTarefaComSucesso() {
        // Pre-condição: Criar uma tarefa para ser finalizada.
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.titulo = "Tarefa a ser finalizada";
        Integer tarefaId = given().contentType(ContentType.JSON).body(tarefaDTO).post("/tarefas").then().extract().path("id");
        
        // Ação: Chamar o endpoint para finalizar.
        given()
            .pathParam("id", tarefaId)
        .when()
            .put("/tarefas/finalizar/{id}")
        .then()
            .statusCode(200)
            .body("finalizado", equalTo(true));
    }

    /**
     * Testa a alocação de uma pessoa a uma tarefa.
     */
    @Test
    public void deveAlocarPessoaNaTarefaComSucesso() {
        // Pre-condição 1: Criar uma pessoa disponível.
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.nome = "Pessoa Disponível";
        pessoaDTO.departamento = "Desenvolvimento";
        Integer pessoaId = given().contentType(ContentType.JSON).body(pessoaDTO).post("/pessoas").then().extract().path("id");

        // Pre-condição 2: Criar uma tarefa pendente no mesmo departamento.
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.titulo = "Tarefa pendente";
        tarefaDTO.departamento = "Desenvolvimento"; // Mesmo departamento da pessoa
        Integer tarefaId = given().contentType(ContentType.JSON).body(tarefaDTO).post("/tarefas").then().extract().path("id");

        // Ação: Alocar a tarefa.
        given()
            .pathParam("id", tarefaId)
        .when()
            .put("/tarefas/alocar/{id}")
        .then()
            .statusCode(200)
            .body("pessoaAlocada.id", equalTo(pessoaId));
    }

    /**
     * Testa a listagem de tarefas pendentes, verificando a ordenação e o limite.
     */
    @Test
    public void deveListarTarefasPendentesCorretamente() {
        // Pre-condição: Criar várias tarefas pendentes com prazos diferentes.
        criarTarefaPendente("Tarefa Urgente", LocalDate.now().plusDays(1));      // 3º mais antigo
        criarTarefaPendente("Tarefa Mais Antiga", LocalDate.now().minusDays(2)); // 1º mais antigo
        criarTarefaPendente("Tarefa Antiga", LocalDate.now().minusDays(1));      // 2º mais antigo
        criarTarefaPendente("Tarefa Distante", LocalDate.now().plusDays(10));    // Não deve aparecer
        
        given()
        .when()
            .get("/tarefas/pendentes")
        .then()
            .statusCode(200)
            .body("$", hasSize(3)) // Verifica se a lista tem exatamente 3 itens.
            .body("[0].titulo", equalTo("Tarefa Mais Antiga")) // Verifica a ordem
            .body("[1].titulo", equalTo("Tarefa Antiga"))
            .body("[2].titulo", equalTo("Tarefa Urgente"));
    }

    /**
     * Método auxiliar para criar tarefas pendentes para o teste acima.
     */
    private void criarTarefaPendente(String titulo, LocalDate prazo) {
        TarefaDTO tarefa = new TarefaDTO();
        tarefa.titulo = titulo;
        tarefa.prazo = prazo;
        given().contentType(ContentType.JSON).body(tarefa).post("/tarefas");
    }
}