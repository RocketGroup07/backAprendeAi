package br.com.aprendeai.model;

import br.com.aprendeai.enums.ArquivoTipo;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id")
    private Atividade atividade;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario enviadoPor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrega_atividade_id")
    private EntregaAtividade entregaAtividade;
    
    private ArquivoTipo tipo;
}