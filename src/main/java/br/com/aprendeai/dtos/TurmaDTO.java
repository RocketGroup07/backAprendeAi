//package br.com.aprendeai.dtos;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import br.com.aprendeai.model.Turma;
//import lombok.Data;
//
//@Data
//public class TurmaDTO {
//	
//	private Long id;
//	private String nome;
//	private int qtdAlunos;
//	private String codigo;
//	private UsuarioDTO professor;
//	private Set<UsuarioDTO> alunos;
//	private LocalDateTime criadoEm;
//	
//	public TurmaDTO(Turma turma) {
//		this.id = turma.getId();
//		this.nome = turma.getNome();
//		this.qtdAlunos = turma.getQtdAlunos();
//		this.codigo = turma.getCodigo();
//		this.criadoEm = turma.getCriadoEm();
//		
//		if(turma.getProfessor() != null) {
//			this.professor = new UsuarioDTO(turma.getProfessor());
//		}
//		
//		if(turma.getAlunos() != null) {
//			this.alunos = turma.getAlunos().stream()
//					.map(UsuarioDTO::new)
//					.collect(Collectors.toSet());
//		}
//		
//	}
//	
//	
//
//}
