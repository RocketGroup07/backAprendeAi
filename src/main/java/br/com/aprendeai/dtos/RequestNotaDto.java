package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Size;

public record RequestNotaDto(
	    @Size(max = 5000)
		String feedback,
		Double nota) {

}
