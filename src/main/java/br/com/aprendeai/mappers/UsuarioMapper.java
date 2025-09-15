package br.com.aprendeai.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.model.Usuario;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
	
	UsuarioResponseDto toResponseDTO(Usuario usuario);
    Usuario toEntityFromCreateDto(UsuarioCreateDto usuarioCreateDto);
    Usuario updateEntityFromCreateDto(@MappingTarget Usuario usuario, UsuarioCreateDto usuarioCreateDto);
    Usuario updateEntityFromUpdateDto(@MappingTarget Usuario usuario, UsuarioUpdateDto usuarioUpdateDto);

}
