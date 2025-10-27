package br.com.aprendeai.model;

import java.time.LocalDateTime;
import java.util.List;

import br.com.aprendeai.enums.StatusAtividade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private boolean entregue;
    
    @Column(length = 5000)
    private String conteudo; // talvez o enunciado da atividade?
    private Double nota;
    
    @Column(length = 5000)
    private String feedback;
    
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Usuario professor;
    
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;
    
    @ManyToMany
    private List<Arquivo> arquivoAnexo;
    
    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL)
    private List<Arquivo> arquivosEntrega;
    
    private String resposta;
    
    @Enumerated(EnumType.STRING)
    private StatusAtividade status = StatusAtividade.PENDENTE;
	
	
	public void entregar() {
		this.entregue = true;
		this.status = StatusAtividade.ENTREGUE;
	}
	
	public void corrigir(Double nota, String feedback) {
		this.nota = nota;
		this.status = StatusAtividade.CORRIGIDA;
		this.feedback = feedback;
	}

}