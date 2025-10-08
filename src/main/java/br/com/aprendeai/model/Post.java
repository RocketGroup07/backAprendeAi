package br.com.aprendeai.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "turma_id")
	private Turma turma;
	
	@OneToOne
	private Usuario autor;
	
	private String titulo;
	private String conteudo;
	
	@Column(name = "data_postagem")
	private LocalDateTime dataPostagem;
	
	private boolean publico = true;
	
	private LocalDateTime dataAgendada;
	
	@OneToOne
    @JoinColumn(name = "arquivo_id")
    private Arquivo post;
	
	
}
