package br.com.aprendeai.service;

import java.util.List;

import br.com.aprendeai.dtos.ComentarioCreateDto;
import br.com.aprendeai.dtos.ComentarioResponseDto;

public interface ComentarioService {
	
	public ComentarioResponseDto criarComentario(Long turmaId, Long postId, ComentarioCreateDto comentarioDto);
	
	public List<ComentarioResponseDto> listarComentarios(Long postId);
	
	public void deletarComentario(Long comentarioId);

}
