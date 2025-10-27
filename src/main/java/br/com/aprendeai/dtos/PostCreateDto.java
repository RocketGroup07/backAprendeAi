package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

public record PostCreateDto(
		String titulo,
	    String conteudo,
	    Boolean publico,
	    LocalDateTime dataPostagem) {
}
