package br.com.aprendeai.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.aprendeai.dtos.EntregaAtividadeResponseDto;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.EntregaAtividade;

@Mapper(componentModel = "spring")
public interface EntregaAtividadeMapper {
	
	@Mapping(source = "atividade.id", target = "atividadeId")
	@Mapping(source = "atividade.titulo", target = "titulo")
    @Mapping(source = "aluno.id", target = "alunoId")
	@Mapping(source = "aluno.nome", target = "alunoNome")
	@Mapping(source = "arquivosEntrega", target = "nomesArquivoEntrega")
    EntregaAtividadeResponseDto toResponseDto(EntregaAtividade entregaAtividade);
    
    List<EntregaAtividadeResponseDto> toResponseDtoList(List<EntregaAtividade> entregas);

    default List<String> map(List<Arquivo> arquivos) {
        if (arquivos == null) return null;
        
        return arquivos.stream()
                .map(Arquivo::getNomeArquivo) 
                .collect(Collectors.toList());
    }

}
