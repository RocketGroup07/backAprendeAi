package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import br.com.aprendeai.model.Usuario;

public record UsuarioResponseDto(
		Long id,
		String nome,
		String login,
		LocalDateTime criadoEm
) {
	
	public static UsuarioResponseDto fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDto(
            usuario.getId(),
            usuario.getNome(),
            usuario.getLogin(),
            usuario.getCriadoEm()
        );
    }
}
