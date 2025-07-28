package br.com.selecao.dto;

/**
 * DTO que representa um departamento com a contagem de pessoas e tarefas.
 * <p>
 * Utilizado para consolidar e exibir os dados agregados por departamento.
 * </p>
 */
public class DepartamentoDTO {
    /**
     * O nome do departamento.
     */
    public String departamento;

    /**
     * O número total de pessoas neste departamento.
     */
    public long quantidadePessoas;

    /**
     * O número total de tarefas associadas a este departamento.
     */
    public long quantidadeTarefas;

    /**
     * Construtor para inicializar o DTO com todos os dados.
     *
     * @param departamento      O nome do departamento.
     * @param quantidadePessoas A contagem de pessoas no departamento.
     * @param quantidadeTarefas A contagem de tarefas no departamento.
     */
    public DepartamentoDTO(String departamento, long quantidadePessoas, long quantidadeTarefas) {
        this.departamento = departamento;
        this.quantidadePessoas = quantidadePessoas;
        this.quantidadeTarefas = quantidadeTarefas;
    }
}