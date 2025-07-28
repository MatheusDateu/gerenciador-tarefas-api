package br.com.selecao.resource;

import br.com.selecao.dto.DepartamentoDTO;
import br.com.selecao.model.Pessoa;
import br.com.selecao.model.Tarefa;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recurso REST para obter informações consolidadas sobre os departamentos.
 * <p>
 * Este endpoint provê uma visão agregada, listando todos os departamentos
 * e a quantidade de pessoas e tarefas associadas a cada um.
 * </p>
 */
@Path("/departamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartamentoResource {
    /**
     * Lista todos os departamentos com a contagem total de pessoas e tarefas.
     * <p>
     * O método coleta todos os dados de pessoas e tarefas, agrupa-os por
     * departamento em memória e retorna um relatório consolidado.
     * </p>
     *
     * @return Uma resposta HTTP 200 OK contendo a lista de DTOs de departamento.
     */
    @GET
    public Response listarDepartamentos() {
        // Passo 1: Busca todas as pessoas e tarefas do banco de dados.
        List<Pessoa> todasAsPessoas = Pessoa.listAll();
        List<Tarefa> todasAsTarefas = Tarefa.listAll();

        // Passo 2: Agrupa e conta as pessoas por departamento usando Streams.
        Map<String, Long> pessoasPorDepto = todasAsPessoas.stream()
                .collect(Collectors.groupingBy(pessoa -> pessoa.departamento, Collectors.counting()));

        // Passo 3: Agrupa e conta as tarefas por departamento usando Streams.
        Map<String, Long> tarefasPorDepto = todasAsTarefas.stream()
                .collect(Collectors.groupingBy(tarefa -> tarefa.departamento, Collectors.counting()));
        
        // Passo 4: Obtém um conjunto único com todos os nomes de departamento.
        Set<String> todosOsDepartamentos = new HashSet<>();
        todosOsDepartamentos.addAll(pessoasPorDepto.keySet());
        todosOsDepartamentos.addAll(tarefasPorDepto.keySet());

        // Passo 5: Constrói a lista final de DTOs para a resposta.
        List<DepartamentoDTO> resultado = new ArrayList<>();
        for (String deptoNome : todosOsDepartamentos) {
            long numPessoas = pessoasPorDepto.getOrDefault(deptoNome, 0L);
            long numTarefas = tarefasPorDepto.getOrDefault(deptoNome, 0L);
            resultado.add(new DepartamentoDTO(deptoNome, numPessoas, numTarefas));
        }

        return Response.ok(resultado).build();
    }
}