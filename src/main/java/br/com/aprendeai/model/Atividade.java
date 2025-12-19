package br.com.aprendeai.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Representa uma atividade com título, datas, comentário e status de entrega.
 */
@Data // -> com essa importação, o get e set são gerados automaticamente
@Entity
@Table(name = "atividades")
public class Atividade {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
    private LocalDateTime dataAtividade;
    private  LocalDateTime dataEntrega;
    
    @Column(length = 5000)
    private String conteudo; // talvez o enunciado da atividade?
    
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Usuario professor;
    
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;
    
    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Arquivo> arquivoAnexo;
    
    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntregaAtividade> entregas;
    
    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritos;

}