package br.com.selecao.resource;

import br.com.selecao.dto.TarefaDTO;
import br.com.selecao.model.Pessoa;
import br.com.selecao.model.Tarefa;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso REST para o gerenciamento de Tarefas.
 * <p>
 * Fornece endpoints para criar e gerenciar as entidades de tarefa,
 * consumindo e produzindo representações em JSON.
 * O caminho base para este recurso é {@code /tarefas}.
 * </p>
 */
@Path("/tarefas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TarefaResource {
    /**
     * Cria uma nova tarefa no sistema.
     *
     * @param tarefaDTO Os dados da nova tarefa.
     * @return Uma resposta HTTP 201 Created com a nova tarefa, ou 404 Not Found se a pessoa informada não existir.
     */
    @POST
    @Transactional
    public Response adicionarTarefa(TarefaDTO tarefaDTO) {
        Pessoa pessoaAlocada = null;
        // Verifica se foi fornecido o ID de uma pessoa para alocar na tarefa.
        if (tarefaDTO.pessoaAlocadaId != null) {
            pessoaAlocada = Pessoa.findById(tarefaDTO.pessoaAlocadaId);
            if (pessoaAlocada == null) {
                // Retorna 404 caso a pessoa especificada para alocação não exista.
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Pessoa com id " + tarefaDTO.pessoaAlocadaId + " não encontrada.")
                        .build();
            }
        }
        
        // Cria uma nova entidade Tarefa a partir do DTO.
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.titulo = tarefaDTO.titulo;
        novaTarefa.descricao = tarefaDTO.descricao;
        novaTarefa.prazo = tarefaDTO.prazo;
        novaTarefa.departamento = tarefaDTO.departamento;
        novaTarefa.duracao = tarefaDTO.duracao;
        novaTarefa.pessoaAlocada = pessoaAlocada;
        novaTarefa.finalizado = false; // Uma nova tarefa sempre começa como não finalizada.

        // Persiste a nova entidade no banco de dados.
        novaTarefa.persist();

        return Response.status(Response.Status.CREATED).entity(novaTarefa).build();
    }

    /**
     * Lista todas as tarefas cadastradas.
     *
     * @return Uma resposta HTTP 200 OK com a lista de todas as tarefas.
     */
    @GET
    public Response listarTarefas() {
        List<Tarefa> tarefas = Tarefa.listAll();
        return Response.ok(tarefas).build();
    }

    /**
     * Marca uma tarefa como finalizada.
     *
     * @param id O ID da tarefa a ser finalizada.
     * @return Uma resposta HTTP 200 OK com a tarefa atualizada, ou 404 Not Found.
     */
    @PUT
    @Path("/finalizar/{id}")
    @Transactional
    public Response finalizarTarefa(@PathParam("id") Long id) {
        Tarefa tarefa = Tarefa.findById(id);
        if (tarefa == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tarefa.finalizado = true;

        return Response.ok(tarefa).build();
    }

    /**
     * Aloca uma tarefa para a primeira pessoa disponível do mesmo departamento.
     *
     * @param id O ID da tarefa a ser alocada.
     * @return Uma resposta HTTP 200 OK com a tarefa atualizada, ou um status de erro.
     */
    @PUT
    @Path("/alocar/{id}")
    @Transactional
    public Response alocarPessoaNaTarefa(@PathParam("id") Long id) {
        Tarefa tarefa = Tarefa.findById(id);
        if (tarefa == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tarefa não encontrada.").build();
        }

        if (tarefa.pessoaAlocada != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Esta tarefa já está alocada.").build();
        }

        // Busca a primeira pessoa disponível no mesmo departamento da tarefa.
        Pessoa pessoaParaAlocar = Pessoa.find("departamento", tarefa.departamento).firstResult();
        
        if (pessoaParaAlocar == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Nenhuma pessoa disponível no departamento: " + tarefa.departamento)
                           .build();
        }

        tarefa.pessoaAlocada = pessoaParaAlocar;

        return Response.ok(tarefa).build();
    }

    /**
     * Lista as 3 tarefas pendentes com os prazos mais antigos.
     * <p>
     * Tarefas pendentes são aquelas que não possuem pessoa alocada.
     * </p>
     *
     * @return Uma resposta HTTP 200 OK com a lista de até 3 tarefas.
     */
    @GET
    @Path("/pendentes")
    public Response listarTarefasPendentes() {
        List<Tarefa> tarefasPendentes = Tarefa.find(
            "pessoaAlocada is null",
            Sort.by("prazo", Sort.Direction.Ascending)
        ).page(0, 3).list();

        return Response.ok(tarefasPendentes).build();
    }
}