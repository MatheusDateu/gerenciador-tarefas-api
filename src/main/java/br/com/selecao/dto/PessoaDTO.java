package br.com.selecao.dto;

/**
 * Objeto de Transferência de Dados (DTO) para criar ou atualizar uma Pessoa.
 * <p>
 * Representa os dados enviados pelo cliente no corpo da requisição.
 * </p>
 */
public class PessoaDTO {
    /**
     * O nome da pessoa a ser criada ou atualizada.
     */
    public String nome;

    /**
     * O departamento ao qual a pessoa pertence.
     */
    public String departamento;
}