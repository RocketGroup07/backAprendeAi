package br.com.aprendeai.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
		Long postId,
	    Long turmaId,
	    String titulo,
	    String conteudo,
	    String autor,
	    LocalDateTime data,
	    List<String> nomeArquivo,
	    List<ComentarioResponseDto> comentarios
		) {

}
