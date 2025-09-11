package br.com.aprendeai.mappers;

import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.model.Usuario;

public interface UsuarioMapper {
	
	UsuarioResponseDto toResponseDTO(Usuario usuario);
    Usuario toEntityFromCreateDto(UsuarioCreateDto usuarioCreateDto);
    Usuario updateEntityFromCreateDto(Usuario usuario, UsuarioCreateDto usuarioCreateDto);
    Usuario updateEntityFromUpdateDto(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto);

}
