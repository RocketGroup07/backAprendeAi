package br.com.aprendeai.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.com.aprendeai.dtos.PostCreateDto;
import br.com.aprendeai.model.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
	
	Post toEntityFromCreateDto(PostCreateDto postCreateDto);
	Post updateEntityFromCreateDto(@MappingTarget Post post, PostCreateDto postCreateDto);
}
