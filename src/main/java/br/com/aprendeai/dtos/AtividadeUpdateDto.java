package br.com.aprendeai.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record AtividadeUpdateDto(
		String titulo,
		LocalDateTime dataEntrega,
		String conteudo,
		List<Long> arquivosAnexosIds
		) {

}
