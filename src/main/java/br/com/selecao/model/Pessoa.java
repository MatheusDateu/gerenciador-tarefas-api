package br.com.selecao.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;

/**
 * Representa uma entidade Pessoa no sistema.
 * <p>
 * Cada {@code Pessoa} possui um nome, está associada a um departamento e
 * pode ter múltiplas tarefas ({@code Tarefa}) atribuídas.
 * </p>
 *
 * <ul>
 * <li><b>nome</b>: O nome da pessoa.</li>
 * <li><b>departamento</b>: O departamento onde a pessoa trabalha.</li>
 * <li><b>tarefas</b>: A lista de tarefas atribuídas a esta pessoa. É uma
 * relação um-para-muitos, onde cada tarefa referencia a pessoa à qual
 * está atribuída.</li>
 * </ul>
 *
 * <p>
 * Esta entidade estende {@code PanacheEntity} para utilizar o padrão
 * <i>active record</i> do Panache, simplificando as operações de banco de dados.
 * </p>
 */
@Entity
public class Pessoa extends PanacheEntity {
    /**
     * O nome da pessoa.
     */
    public String nome;

    /**
     * O departamento onde a pessoa trabalha.
     */
    public String departamento;

    /**
     * A lista de tarefas atribuídas a esta pessoa.
     * <p>
     * A anotação {@code @OneToMany} define a relação de um-para-muitos.
     * <ul>
     * <li><b>mappedBy = "pessoaAlocada"</b>: Indica que o lado "dono" da
     * relação está na entidade {@code Tarefa}, no campo {@code pessoaAlocada}.</li>
     * <li><b>fetch = FetchType.LAZY</b>: As tarefas só serão carregadas do
     * banco de dados quando a lista for acessada pela primeira vez.</li>
     * </ul>
     * </p>
     */
    @OneToMany(mappedBy = "pessoaAlocada", fetch = FetchType.LAZY)
    public List<Tarefa> tarefas;
}