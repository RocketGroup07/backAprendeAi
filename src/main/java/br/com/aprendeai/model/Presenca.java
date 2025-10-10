package br.com.aprendeai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "presencas")
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dia_aula_id", nullable = false)
    private DiaAula diaAula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @Column(name = "horas_presente", nullable = false)
    private Integer horasPresente;

    public Presenca() {}

    public Presenca(DiaAula diaAula, Usuario aluno, Integer horasPresente) {
        this.diaAula = diaAula;
        this.aluno = aluno;
        this.horasPresente = horasPresente;
    }

    public Long getId() { return id; }
    public DiaAula getDiaAula() { return diaAula; }
    public Usuario getAluno() { return aluno; }
    public Integer getHorasPresente() { return horasPresente; }
    public void setHorasPresente(Integer horasPresente) { this.horasPresente = horasPresente; }
}
