package br.com.aprendeai.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.ComentarioResponseDto;
import br.com.aprendeai.model.Comentario;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComentarioMapper {

	@Mapping(source = "usuario.nome", target = "autor")
    ComentarioResponseDto toResponseDto(Comentario comentario);
}
