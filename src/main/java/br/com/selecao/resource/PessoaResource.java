package br.com.selecao.resource;

import br.com.selecao.dto.PessoaComHorasDTO;
import br.com.selecao.dto.PessoaDTO;
import br.com.selecao.dto.PessoaMediaHorasDTO;
import br.com.selecao.model.Pessoa;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recurso REST para gerenciar as Pessoas no sistema.
 * <p>
 * Fornece endpoints para operações CRUD (Criar, Ler, Atualizar, Apagar)
 * e também para consultas e relatórios customizados sobre as pessoas.
 * </p>
 */
@Path("/pessoas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PessoaResource {
    /**
     * Lista todas as pessoas, incluindo o total de horas gastas em suas tarefas.
     * <p>
     * Este método calcula a soma da duração de todas as tarefas para cada pessoa.
     * </p>
     *
     * @return Uma resposta HTTP 200 OK com a lista de pessoas e suas horas totais.
     */
    @GET
    public Response listarPessoas() {
        List<Pessoa> todasAsPessoas = Pessoa.listAll();

        // Transforma a lista de entidades Pessoa em uma lista de DTOs.
        List<PessoaComHorasDTO> resultado = todasAsPessoas.stream().map(pessoa -> {
            // Para cada pessoa, soma a duração de suas tarefas.
            int totalHoras = pessoa.tarefas.stream()
                                           .mapToInt(tarefa -> tarefa.duracao)
                                           .sum();
            
            // Cria o DTO com os dados calculados.
            return new PessoaComHorasDTO(pessoa.nome, pessoa.departamento, totalHoras);
        }).collect(Collectors.toList());

        return Response.ok(resultado).build();
    }

    /**
     * Busca uma única pessoa pelo seu ID.
     *
     * @param id O ID da pessoa a ser encontrada.
     * @return Uma resposta HTTP 200 OK com os dados da pessoa, ou 404 Not Found caso não encontre.
     */
    @GET
    @Path("/{id}")
    public Response buscarPessoaPorId(@PathParam("id") Long id) {
        Pessoa pessoa = Pessoa.findById(id);
        if (pessoa == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pessoa).build();
    }

    /**
     * Busca pessoas por nome e período, retornando a média de horas gastas por tarefa.
     *
     * @param nome          O nome da pessoa para buscar (busca parcial, ignora maiúsculas/minúsculas).
     * @param dataInicioStr A data de início do período (formato AAAA-MM-DD).
     * @param dataFimStr    A data de fim do período (formato AAAA-MM-DD).
     * @return Uma lista de pessoas que correspondem aos critérios com sua média de horas por tarefa.
     */
    @GET
    @Path("/gastos")
    public Response buscarGastosPorPessoa(
            @QueryParam("nome") String nome,
            @QueryParam("dataInicio") String dataInicioStr,
            @QueryParam("dataFim") String dataFimStr) {

        LocalDate dataInicio;
        LocalDate dataFim;

        // Converte as datas recebidas como String e trata possíveis erros de formato.
        try {
            dataInicio = LocalDate.parse(dataInicioStr);
            dataFim = LocalDate.parse(dataFimStr);
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Formato de data inválido. Use o padrão AAAA-MM-DD.")
                           .build();
        }

        PanacheQuery<Pessoa> query = Pessoa.find("lower(nome) like ?1", "%" + nome.toLowerCase() + "%");
        List<Pessoa> pessoasEncontradas = query.list();

        List<PessoaMediaHorasDTO> resultado = pessoasEncontradas.stream().map(pessoa -> {
            
            // Calcula a média de horas das tarefas da pessoa dentro do período especificado.
            double media = pessoa.tarefas.stream()
                .filter(tarefa -> !tarefa.prazo.isBefore(dataInicio) && !tarefa.prazo.isAfter(dataFim))
                .mapToInt(tarefa -> tarefa.duracao)
                .average()
                .orElse(0.0);

            return new PessoaMediaHorasDTO(pessoa.nome, media);

        }).collect(Collectors.toList());

        return Response.ok(resultado).build();
    }

    /**
     * Cria uma nova Pessoa.
     * O método é transacional para garantir a integridade dos dados.
     *
     * @param pessoaDTO Os dados da nova pessoa, recebidos do corpo da requisição.
     * @return Uma resposta HTTP 201 Created com os dados da pessoa recém-criada.
     */
    @POST
    @Transactional
    public Response adicionarPessoa(PessoaDTO pessoaDTO) {
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.nome = pessoaDTO.nome;
        novaPessoa.departamento = pessoaDTO.departamento;

        novaPessoa.persist();

        return Response.status(Response.Status.CREATED).entity(novaPessoa).build();
    }

    /**
     * Atualiza os dados de uma pessoa existente.
     *
     * @param id        O ID da pessoa a ser atualizada.
     * @param pessoaDTO Os novos dados para a pessoa.
     * @return Uma resposta HTTP 200 OK com os dados da pessoa atualizada, ou 404 Not Found.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response alterarPessoa(@PathParam("id") Long id, PessoaDTO pessoaDTO) {
        Pessoa pessoa = Pessoa.findById(id);
        if (pessoa == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Atualiza os campos da entidade com os dados do DTO.
        pessoa.nome = pessoaDTO.nome;
        pessoa.departamento = pessoaDTO.departamento;
        
        // As alterações são salvas automaticamente ao final da transação.
        
        return Response.ok(pessoa).build();
    }

    /**
     * Remove uma pessoa pelo seu ID.
     *
     * @param id O ID da pessoa a ser removida, recebido do caminho da URL.
     * @return Uma resposta HTTP 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response removerPessoa(@PathParam("id") Long id) {
        Pessoa pessoa = Pessoa.findById(id);

        if (pessoa != null) {
            pessoa.delete();
            // A resposta padrão para um DELETE bem-sucedido é 204 No Content.
            return Response.noContent().build();
        }

        // Caso a pessoa não seja encontrada, retorna 404 Not Found.
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}