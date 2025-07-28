package br.com.selecao.dto;

/**
 * DTO que representa uma pessoa com a média de horas gastas por tarefa.
 * <p>
 * Utilizado para retornar dados de uma pessoa com a média de horas calculada
 * para um período específico.
 * </p>
 */
public class PessoaMediaHorasDTO {
    /**
     * O nome da pessoa.
     */
    public String nome;

    /**
     * A média de horas que a pessoa gasta por tarefa.
     */
    public double mediaHorasPorTarefa;

    /**
     * Construtor para inicializar o DTO.
     *
     * @param nome                O nome da pessoa.
     * @param mediaHorasPorTarefa A média de horas por tarefa calculada.
     */
    public PessoaMediaHorasDTO(String nome, double mediaHorasPorTarefa) {
        this.nome = nome;
        this.mediaHorasPorTarefa = mediaHorasPorTarefa;
    }
}