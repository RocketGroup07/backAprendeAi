package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

public record PostCreateDto(
		String titulo,
		 @Size(max = 5000)
	    String conteudo,
	    Boolean publico,
	    LocalDateTime dataPostagem) {
}
