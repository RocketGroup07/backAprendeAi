//package br.com.aprendeai.model;
//
//import java.time.LocalDate;
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
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "tarefas")
//public class Tarefa {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	@ManyToOne
//	@JoinColumn(name = "turma_id")
//	private Turma turma;
//	
//	private String titulo;
//	
//	private String descricao;
//	
//	private LocalDate dataEntrega;
//	
//	private LocalDateTime publicadoEm = LocalDateTime.now();
//	
//	@ManyToMany
//	@JoinTable(
//			name = "tarefas_arquivos",
//			joinColumns = @JoinColumn(name = "tarefa_id"),
//			inverseJoinColumns = @JoinColumn(name = "arquivo_id"))
//	private List<Arquivo> arquivos;
//}
