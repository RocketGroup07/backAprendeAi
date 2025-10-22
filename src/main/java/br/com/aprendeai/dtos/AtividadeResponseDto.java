package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

public record AtividadeResponseDto(
		Long id,
	    String titulo,
	    String conteudo,
	    LocalDateTime dataEntrega,
	    boolean entregue,
	    String professorNome,
	    String turmaNome
		
		) {

}
