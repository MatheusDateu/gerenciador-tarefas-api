package br.com.selecao.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * Representa uma entidade Tarefa no sistema.
 * <p>
 * Cada {@code Tarefa} contém informações sobre seu título, descrição, prazo,
 * departamento, duração, status de finalização e a pessoa alocada a ela.
 * </p>
 *
 * <ul>
 * <li><b>titulo</b>: O título da tarefa.</li>
 * <li><b>descricao</b>: Uma descrição detalhada da tarefa.</li>
 * <li><b>prazo</b>: A data limite para a conclusão da tarefa.</li>
 * <li><b>departamento</b>: O departamento responsável pela tarefa.</li>
 * <li><b>duracao</b>: A duração da tarefa em minutos.</li>
 * <li><b>finalizado</b>: Indica se a tarefa foi finalizada.</li>
 * <li><b>pessoaAlocada</b>: A pessoa alocada para esta tarefa.</li>
 * </ul>
 *
 * <p>
 * Esta entidade estende {@link PanacheEntity} para utilizar o padrão
 * <i>active record</i> do Panache.
 * </p>
 */
@Entity
public class Tarefa extends PanacheEntity {
    /**
     * O título da tarefa.
     */
    public String titulo;

    /**
     * Uma descrição detalhada da tarefa.
     */
    public String descricao;

    /**
     * A data limite para a conclusão da tarefa.
     */
    public LocalDate prazo;

    /**
     * O departamento responsável pela tarefa.
     */
    public String departamento;

    /**
     * A duração da tarefa em minutos.
     */
    public int duracao;

    /**
     * Um indicador que informa se a tarefa foi finalizada.
     */
    public boolean finalizado;

    /**
     * A pessoa alocada para executar esta tarefa.
     * <p>
     * A anotação {@code @ManyToOne} define a relação de muitos-para-um,
     * indicando que muitas tarefas podem estar associadas a uma pessoa.
     * </p>
     */
    @ManyToOne
    public Pessoa pessoaAlocada;
}