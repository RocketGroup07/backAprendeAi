package br.com.aprendeai.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.AtividadeCreateDto;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.model.Atividade;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AtividadeMapper {
	
	AtividadeResponseDto toResponseDTO(Atividade atividade);
	Atividade toEntityFromCreateDto(AtividadeCreateDto atividadeCreateDto);
	Atividade updateEntityFromCreateDto(@MappingTarget Atividade atividade, AtividadeCreateDto atividadeCreateDto);

}
