package br.com.aprendeai.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.TurmaCreateDto;
import br.com.aprendeai.dtos.TurmaResponseDto;
import br.com.aprendeai.dtos.TurmaUpdateDto;
import br.com.aprendeai.model.Turma;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TurmaMapper {
	
	TurmaResponseDto toResponseDto(Turma turma);
	Turma toEntityFromCreateDto(TurmaCreateDto turmaCreateDto);
	Turma updateEntityFromCreateDto(@MappingTarget Turma turma, TurmaCreateDto turmaCreateDto);
	Turma updateEntityFromUpdateDto(@MappingTarget Turma turma, TurmaUpdateDto turmaUpdateDto);

}
