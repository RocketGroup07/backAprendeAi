package br.com.aprendeai.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int limiteAlunos;
    private String codigo;

    // NOVO CAMPO: carga horária total do curso
    @Column(name = "carga_horaria_total", nullable = false)
    private int cargaHorariaTotal;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Usuario professor;
    
    
    @OneToMany(mappedBy = "turma", cascade = CascadeType.REMOVE)
    private List<Atividade> atividades;

    @ManyToMany
    @JoinTable(
            name = "turma_alunos",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private Set<Usuario> alunos = new HashSet<>();

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
}
