package br.com.selecao.dto;

import java.time.LocalDate;

/**
 * Objeto de Transferência de Dados (DTO) que representa uma tarefa (Tarefa).
 * <p>
 * Esta classe é utilizada para transferir dados de tarefas entre as diferentes
 * camadas da aplicação, como da requisição da API para a lógica de negócio.
 * </p>
 *
 * <ul>
 * <li><b>titulo</b>: O título da tarefa.</li>
 * <li><b>descricao</b>: Uma descrição detalhada da tarefa.</li>
 * <li><b>prazo</b>: A data limite para a conclusão da tarefa.</li>
 * <li><b>departamento</b>: O departamento responsável pela tarefa.</li>
 * <li><b>duracao</b>: A duração da tarefa em minutos.</li>
 * <li><b>pessoaAlocadaId</b>: O ID da pessoa alocada para a tarefa.</li>
 * </ul>
 */
public class TarefaDTO {
    /**
     * O título da tarefa.
     */
    public String titulo;

    /**
     * Descrição detalhada da tarefa.
     */
    public String descricao;

    /**
     * Data limite para conclusão da tarefa.
     */
    public LocalDate prazo;

    /**
     * Nome do departamento associado à tarefa.
     */
    public String departamento;

    /**
     * Duração da tarefa em minutos.
     */
    public int duracao;

    /**
     * ID da pessoa alocada para a tarefa.
     * <p>
     * Este campo é usado para associar a tarefa a uma pessoa específica.
     * </p>
     */
    public Long pessoaAlocadaId;
}