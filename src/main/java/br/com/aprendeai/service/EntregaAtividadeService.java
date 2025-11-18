package br.com.aprendeai.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.model.Arquivo;

public interface EntregaAtividadeService {

	public AtividadeResponseDto entregarAtividade(Long atividadeId, String resposta, MultipartFile arquivo);
	
	public List<AtividadeResponseDto> listarAtividadesEntregues(Long turmaId);
	
	public AtividadeResponseDto corrigirAtividade(Long atividadeId, RequestNotaDto dto);
	
	AtividadeResponseDto editarEntrega(Long atividadeId, String novaResposta, MultipartFile novoArquivo);

	void excluirEntrega(Long atividadeId);

	Arquivo baixarEntregaDeAluno(Long atividadeId, Long alunoId);
}
