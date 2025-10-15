package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

public record AtividadeResponseDto(
		Long id,
	    String titulo,
	    LocalDateTime dataEntrega,
	    boolean entregue,
	    String professorNome,
	    String turmaNome
		
		) {

}
