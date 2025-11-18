package br.com.aprendeai.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record AtividadeResponseDto(
		Long id,
	    String titulo,
	    String conteudo,
	    LocalDateTime dataAtividade,
	    LocalDateTime dataEntrega,
	    String professorNome,
	    String turmaNome,
		List<String> nomesArquivosAnexo,
		List<EntregaAtividadeResponseDto> entrega
		) {

}
