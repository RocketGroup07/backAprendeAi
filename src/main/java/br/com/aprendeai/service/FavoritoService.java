package br.com.aprendeai.service;

import java.util.List;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.FavoritosResponseDto;
import br.com.aprendeai.dtos.PostResponseDto;

public interface FavoritoService {
	
	public PostResponseDto favoritar(Long postId);
	
	public List<PostResponseDto> listarFavoritos();
	
	public AtividadeResponseDto favoritarAtv(Long atividadeId);
	
	public List<AtividadeResponseDto> listarAtividadesFavs();

	public FavoritosResponseDto listarTodosFavoritos();

	void removerFavoritoPost(Long postId);

	void removerFavoritoAtividade(Long atividadeId);

}
