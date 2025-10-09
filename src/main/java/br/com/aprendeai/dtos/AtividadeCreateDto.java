package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

public record AtividadeCreateDto(
		String titulo,
		LocalDateTime dataEntrega,
		String conteudo) {

}
