package br.com.aprendeai.dtos;

import java.util.List;

public record FavoritosResponseDto(
		List<PostResponseDto> posts,
	    List<AtividadeResponseDto> atividades
	    ) {

}
