package br.com.aprendeai.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.PostResponseDto;

public interface PostService {

	public PostResponseDto criarPost(Long usuarioId, Long turmaId, String post, MultipartFile arquivo);
	
	public PostResponseDto buscarPostPorId(Long postId, Long turmaId);
	
	public List<PostResponseDto> listarPostsDaTurma(Long turmaId);
	
	public List<PostResponseDto> buscarNaTurmaPeloTitulo(Long turmaId, String titulo);
	
	public void deletarPost(Long turmaId, Long postId);
}
