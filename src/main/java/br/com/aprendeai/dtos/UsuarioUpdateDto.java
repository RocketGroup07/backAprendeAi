package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDto(
		
		@NotBlank(message = "Nome é obrigatório")
	    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
	    String nome,
	    
	    @NotBlank(message = "Email é obrigatório")
	    @Email(message = "Email deve ser válido")
	    @Size(max = 50, message = "Email deve ter no máximo 50 caracteres")
	    String login,
	    
	    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
	    String senha
		
		) {

}
