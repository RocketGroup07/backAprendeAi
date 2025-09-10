package br.com.aprendeai.dtos;

import br.com.aprendeai.enums.PapelEnum;

public record UsuarioDto(
		String nome,
		String login,
		String senha,
		PapelEnum papel) {

}
