package br.com.aprendeai.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.EntregaAtividadeResponseDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.model.Arquivo;

public interface EntregaAtividadeService {

	public AtividadeResponseDto entregarAtividade(Long atividadeId, String resposta, MultipartFile arquivo);
	
	public AtividadeResponseDto corrigirAtividade(Long atividadeId, Long alunoId, RequestNotaDto dto);
	
	AtividadeResponseDto editarEntrega(Long atividadeId, String novaResposta, MultipartFile novoArquivo);

	void excluirEntrega(Long atividadeId);

	Arquivo baixarEntregaDeAluno(Long atividadeId, Long alunoId);

	List<EntregaAtividadeResponseDto> listarEntregasPorAtividadeParaProfessor(Long atividadeId);

	EntregaAtividadeResponseDto verMinhaEntrega(Long atividadeId);
}
