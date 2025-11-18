package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Size;

public record EntregaAtividadeRequestDto(
		 @Size(max = 5000)
		String resposta
		) {

}
