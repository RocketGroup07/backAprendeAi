package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.model.Usuario;

public record UsuarioResponseDto(
		Long id,
		String nome,
		String login,
		LocalDateTime criadoEm,
		PapelEnum papel
) {
	
	public static UsuarioResponseDto fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDto(
            usuario.getId(),
            usuario.getNome(),
            usuario.getLogin(),
            usuario.getCriadoEm(),
            usuario.getPapel()
        );
    }
}
