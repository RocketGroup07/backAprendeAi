package br.com.aprendeai.dtos;

import java.time.LocalDateTime;
import java.util.List;

import br.com.aprendeai.enums.StatusAtividade;

public record AtividadeResponseDto(
		Long id,
	    String titulo,
	    String conteudo,
	    LocalDateTime dataAtividade,
	    LocalDateTime dataEntrega,
	    boolean entregue,
	    String professorNome,
	    String turmaNome,
		List<String> nomesArquivosAnexo,
		StatusAtividade status,
		Double nota
		) {

}
