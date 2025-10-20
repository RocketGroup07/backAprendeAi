package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

public record PostResponseDto(
		Long postId,
	    Long turmaId,
	    String titulo,
	    String conteudo,
	    String autor,
	    LocalDateTime data
		) {

}
