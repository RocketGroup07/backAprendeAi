package br.com.aprendeai.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
public class DiaAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_aula", nullable = false)
    private LocalDate dataAula;

    @Column(name = "conteudo", nullable = false)
    private String conteudo;

    @Column(name = "horas_maximas", nullable = false)
    private Integer horasMaximas;

    @Column(name = "horas_totais", nullable = false)
    private Integer horasTotais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    public DiaAula() {}

    public DiaAula(LocalDate dataAula, String conteudo, Integer horasMaximas, Integer horasTotais, Turma turma) {
        this.dataAula = dataAula;
        this.conteudo = conteudo;
        this.horasMaximas = horasMaximas;
        this.horasTotais = horasTotais;
        this.turma = turma;
    }

    public Long getId() { return id; }

    public LocalDate getDataAula() { return dataAula; }
    public void setDataAula(LocalDate dataAula) { this.dataAula = dataAula; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public Integer getHorasMaximas() { return horasMaximas; }
    public void setHorasMaximas(Integer horasMaximas) { this.horasMaximas = horasMaximas; }

    public Integer getHorasTotais() { return horasTotais; }
    public void setHorasTotais(Integer horasTotais) { this.horasTotais = horasTotais; }

    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
}
