package br.com.aprendeai.dtos;

public record LoginResponseDto(
		String token,
        UsuarioResponseDto usuario,
        String mensagem
        ) {

}
