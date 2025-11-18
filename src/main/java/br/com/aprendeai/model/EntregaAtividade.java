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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class EntregaAtividade {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne 
    @JoinColumn(name = "atividade_id")
    private Atividade atividade;
    
    @ManyToOne 
    @JoinColumn(name = "aluno_id")
    private Usuario aluno;
    
    private LocalDateTime dataEntrega = LocalDateTime.now();
    
    @Column(length = 5000)
    private String respostaTexto; 
    
    @OneToMany(mappedBy = "entregaAtividade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Arquivo> arquivosEntrega;
    
    private Double nota;
    
    @Column(length = 5000)
    private String feedback;
    
    @Enumerated(EnumType.STRING)
    private StatusAtividade status = StatusAtividade.ENTREGUE;
    
    private boolean entregue;
    
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
