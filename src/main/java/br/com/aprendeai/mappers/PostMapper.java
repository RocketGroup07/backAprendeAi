package br.com.aprendeai.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.PostCreateDto;
import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
	
	@Mapping(source = "id", target = "postId")
    @Mapping(source = "turma.id", target = "turmaId")
    @Mapping(source = "autor.nome", target = "autor")
    @Mapping(source = "dataPostagem", target = "data")
	@Mapping(source = "arquivo", target = "nomeArquivo")
	@Mapping(source = "comentarios", target = "comentarios")
    PostResponseDto toResponseDto(Post post);
	Post toEntityFromCreateDto(PostCreateDto postCreateDto);
	Post updateEntityFromCreateDto(@MappingTarget Post post, PostCreateDto postCreateDto);
	
	default List<String> mapArquivosToNomes(List<Arquivo> arquivos) {
	    if (arquivos == null) return null;
	    return arquivos.stream()
	            .map(Arquivo::getNomeArquivo)
	            .collect(Collectors.toList());
	}
}
