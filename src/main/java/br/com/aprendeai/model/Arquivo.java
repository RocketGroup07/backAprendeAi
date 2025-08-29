//package br.com.aprendeai.model;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//
//@Entity
//@Data
//public class Arquivo {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	private String nomeOriginal;
//	private String caminhoArquivo;
//	private String mimeType;
//	private Long tamanho;
//	
//	@ManyToOne
//	@JoinColumn(name = "pload_por_usuario_id")
//	private Usuario usuario;
//	
//	@Column(name = "upload_em")
//	private LocalDateTime uploadEm = LocalDateTime.now();
//}
