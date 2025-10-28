package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

public record AtividadeUpdateDto(
		String titulo,
		LocalDateTime dataEntrega,
		@Size(max = 5000)
		String conteudo,
		String feedback
		) {

}
