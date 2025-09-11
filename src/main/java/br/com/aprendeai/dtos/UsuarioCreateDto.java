package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCreateDto(
		
		@NotBlank(message = "Nome é obrigatório")
	    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
		String nome, 
		
		@NotBlank(message = "Email é obrigatório")
	    @Email(message = "Email deve ser válido")
		String login,
		
		@NotBlank(message = "Senha é obrigatória")
		@Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
		String senha
) {}
