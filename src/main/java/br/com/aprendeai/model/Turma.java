package br.com.aprendeai.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "turmas")
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int qtdAlunos;
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Usuario professor;

    @ManyToMany
    @JoinTable(
            name = "turma_alunos",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private Set<Usuario> alunos;
    
    @Column(name = "criado_em")
	private LocalDateTime criadoEm;
}