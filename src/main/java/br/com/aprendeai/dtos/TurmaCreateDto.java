package br.com.aprendeai.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TurmaCreateDto(
		 @NotBlank(message = "Nome é obrigatório")
		 String nome,
		
		 @NotNull(message = "Limite de alunos é obrigatório")
		 Integer limiteAlunos
) {

}
