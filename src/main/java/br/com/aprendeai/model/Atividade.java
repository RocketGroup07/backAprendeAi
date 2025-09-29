package br.com.aprendeai.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private String conteudo; // talvez o enunciado da atividade?
    
//    @OneToOne
//    @JoinColumn(name = "professor_id")
//    private Usuario professor;
//    
//    @OneToOne
//    @JoinColumn(name = "turma_id")
//    private Turma turma;

    
    /**
     * Define o comentário da atividade.
     * @param comentario Comentário do usuário.
     */
//    public void setComentario(String comentario) {
//        if (comentario == null) throw new IllegalArgumentException("Comentário não pode ser nulo");
//        this.comentario = comentario;
//    }

    /**
     * Marca a atividade como entregue.
     */
//    public void entregar() {
//        this.entregue = true;
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Atividade)) return false;
        Atividade other = (Atividade) obj;
        return titulo.equalsIgnoreCase(other.titulo);
    }

    @Override
    public int hashCode() {
        return titulo.toLowerCase().hashCode();
    }

	public Atividade(Long id, String titulo, LocalDateTime dataAtividade, LocalDateTime dataEntrega, boolean entregue,
			String conteudo, Usuario professor, Turma turma) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.dataAtividade = dataAtividade;
		this.dataEntrega = dataEntrega;
		this.entregue = false;
		this.conteudo = conteudo;
//		this.professor = professor;
//		this.turma = turma;
	}
}