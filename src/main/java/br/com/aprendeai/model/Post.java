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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@ManyToOne
	private Usuario autor;
	
	private String titulo;
	
	@Column(length = 5000)
	private String conteudo;
	
	@Column(name = "data_postagem")
	private LocalDateTime dataPostagem;
	
	private boolean publico = true;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Arquivo> arquivo;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comentario> comentarios;
	
	
}
