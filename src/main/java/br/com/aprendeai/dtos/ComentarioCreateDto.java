package br.com.aprendeai.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioCreateDto(
		@NotBlank
	    @Size(max = 5000)
		String conteudo) {

}
