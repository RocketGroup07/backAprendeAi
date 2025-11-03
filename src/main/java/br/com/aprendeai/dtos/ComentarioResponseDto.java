package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

public record ComentarioResponseDto(
		Long id,
	    String conteudo,
	    String usuario,
	    LocalDateTime dataComentario) {

}
