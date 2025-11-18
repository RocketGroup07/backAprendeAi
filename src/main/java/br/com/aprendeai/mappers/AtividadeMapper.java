package br.com.aprendeai.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.AtividadeCreateDto;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeUpdateDto;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.EntregaAtividade;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE
//			uses = {EntregaAtividadeMapper.class}
)
public interface AtividadeMapper {
	
	@Mapping(source = "professor.nome", target = "professorNome")
	@Mapping(source = "turma.nome", target = "turmaNome")
	@Mapping(source = "arquivoAnexo", target = "nomesArquivosAnexo")
	@Mapping(source = "entregas", target = "entrega")
	AtividadeResponseDto toResponseDTO(Atividade atividade);
	
//	@Mapping(source = "atividade.professor.nome", target = "professorNome")
//    @Mapping(source = "atividade.turma.nome", target = "turmaNome")
//    @Mapping(source = "atividade.arquivoAnexo", target = "nomesArquivosAnexo")
//    @Mapping(source = "entregas", target = "entrega") 
//	AtividadeResponseDto toResponseDTO(Atividade atividade, EntregaAtividade entrega);
	
	Atividade toEntityFromCreateDto(AtividadeCreateDto atividadeCreateDto);
	
	Atividade updateEntityFromCreateDto(@MappingTarget Atividade atividade, AtividadeCreateDto atividadeCreateDto);
	
	Atividade updateEntityFromUpdateDto(@MappingTarget Atividade atividade, AtividadeUpdateDto atividadeUpdateDto);
	
	default List<String> mapArquivosParaNomes(List<Arquivo> arquivos){
		if(arquivos == null) return null;
		return arquivos.stream()
				.map(Arquivo::getNomeArquivo)
				.collect(Collectors.toList());
	}
}
