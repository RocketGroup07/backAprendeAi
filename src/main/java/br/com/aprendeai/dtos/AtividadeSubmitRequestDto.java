package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Size;

public record AtividadeSubmitRequestDto(
		 @Size(max = 5000)
		String resposta
		) {

}
