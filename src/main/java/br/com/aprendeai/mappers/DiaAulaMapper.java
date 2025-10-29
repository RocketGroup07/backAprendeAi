package br.com.aprendeai.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import br.com.aprendeai.dtos.DiaAulaCreateDTO;
import br.com.aprendeai.dtos.DiaAulaResponseDTO;
import br.com.aprendeai.model.DiaAula;

@Mapper(componentModel = "spring")
public interface DiaAulaMapper {

    @Mapping(target = "turma.id", source = "turmaId")
    DiaAula toEntity(DiaAulaCreateDTO dto);

    @Mapping(target = "turmaId", source = "turma.id")
    DiaAulaResponseDTO toResponse(DiaAula diaAula);
}
