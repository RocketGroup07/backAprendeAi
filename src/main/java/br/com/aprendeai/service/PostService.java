package br.com.aprendeai.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.model.Arquivo;

public interface PostService {

	public PostResponseDto criarPost(Long usuarioId, Long turmaId, String post, MultipartFile arquivo);
	
	public PostResponseDto buscarPostPorId(Long postId, Long turmaId);
	
	public List<PostResponseDto> listarPostsDaTurma(Long turmaId);
	
	public List<PostResponseDto> buscarNaTurmaPeloTitulo(Long turmaId, String titulo);
	
	public void deletarPost(Long turmaId, Long postId);

	Arquivo baixarAnexo(Long postId, Long turmaId);
}
