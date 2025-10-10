package br.com.aprendeai.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dias_aula", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"turma_id", "data_aula"})
})
public class DiaAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_aula", nullable = false)
    private LocalDate dataAula;

    @Column(name = "horas_maximas", nullable = false)
    private Integer horasMaximas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    public DiaAula() {}

    public DiaAula(LocalDate dataAula, Integer horasMaximas, Turma turma) {
        this.dataAula = dataAula;
        this.horasMaximas = horasMaximas;
        this.turma = turma;
    }

    public Long getId() { return id; }
    public LocalDate getDataAula() { return dataAula; }
    public void setDataAula(LocalDate dataAula) { this.dataAula = dataAula; }
    public Integer getHorasMaximas() { return horasMaximas; }
    public void setHorasMaximas(Integer horasMaximas) { this.horasMaximas = horasMaximas; }
    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
}
