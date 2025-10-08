package br.com.aprendeai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeArquivo;
    private String tipoArquivo;
    private String caminhoArquivo; 

//    @ManyToOne
//    @JoinColumn(name = "atividade_id")
//    private Atividade atividade;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario enviadoPor;
}