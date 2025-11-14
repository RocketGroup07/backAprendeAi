package br.com.aprendeai.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeUpdateDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.model.Arquivo;

public interface AtividadeService {
	
	public AtividadeResponseDto criarAtividade(Long turmaId, String Atividade, MultipartFile arquivo);
	
	public List<AtividadeResponseDto> listarAtividades(Long turmaId);
	
	public List<AtividadeResponseDto> listarAtividadesEntregues(Long turmaId);
	
	public Arquivo baixarAnexo(Long atividadeId);
	
	public AtividadeResponseDto entregarAtividade(Long atividadeId, String resposta, MultipartFile arquivo);
	
	public AtividadeResponseDto corrigirAtividade(Long atividadeId, RequestNotaDto dto);
	
	public AtividadeResponseDto atualizarAtividade(Long id, AtividadeUpdateDto atividadeAtualizada);
	
	public void deletarAtividade(Long id);

	AtividadeResponseDto buscarPorId(Long atividadeId);

	AtividadeResponseDto editarEntrega(Long atividadeId, String novaResposta, MultipartFile novoArquivo);

	void excluirEntrega(Long atividadeId);

	Arquivo baixarEntregaDeAluno(Long atividadeId, Long alunoId);

}
