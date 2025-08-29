//package br.com.aprendeai.model;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "materiais")
//public class Material {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	private Turma turma;
//	
//	private String titulo;
//	private String conteudo;
//	
//	private LocalDateTime publicadoEm = LocalDateTime.now();
//	
//	@ManyToMany
//	@JoinTable(
//			name = "materiais_arquivos",
//			joinColumns = @JoinColumn(name = "material_id"),
//			inverseJoinColumns = @JoinColumn(name = "arquivo_id")
//	)
//	private List<Arquivo> arquivos;
//}
