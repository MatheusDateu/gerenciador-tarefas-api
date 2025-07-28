package br.com.selecao.dto;

/**
 * DTO que representa uma pessoa com o total de horas gastas em suas tarefas.
 * <p>
 * Esta classe é usada para retornar uma visão agregada dos dados de uma pessoa,
 * incluindo um campo calculado que não existe na entidade original.
 * </p>
 */
public class PessoaComHorasDTO {
    /**
     * O nome da pessoa.
     */
    public String nome;

    /**
     * Nome do departamento ao qual a pessoa está associada.
     */
    public String departamento;

    /**
     * Representa o total de horas gastas pela pessoa em todas as suas tarefas.
     */
    public int totalHorasGastas;

    /**
     * Construtor para facilitar a criação do DTO.
     *
     * @param nome             O nome da pessoa.
     * @param departamento     O departamento da pessoa.
     * @param totalHorasGastas O total de horas calculado a partir das tarefas.
     */
    public PessoaComHorasDTO(String nome, String departamento, int totalHorasGastas) {
        this.nome = nome;
        this.departamento = departamento;
        this.totalHorasGastas = totalHorasGastas;
    }
}