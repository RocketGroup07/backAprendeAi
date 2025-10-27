package br.com.aprendeai.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.model.Atividade;

public interface AtividadeService {
	
	public AtividadeResponseDto criarAtividade(Long turmaId, String Atividade, MultipartFile arquivo);
	
	public List<AtividadeResponseDto> listarAtividades(Long turmaId);
	
	public Resource baixarAnexo(Long atividadeId);
	
	public AtividadeResponseDto entregarAtividade(Long atividadeId, String respostaJson, MultipartFile arquivo);
	
	public AtividadeResponseDto corrigirAtividade(Long atividadeId, RequestNotaDto dto);
	
	public AtividadeResponseDto atualizarAtividade(Long id, Atividade atividadeAtualizada);
	
	public void deletarAtividade(Long id);

}
