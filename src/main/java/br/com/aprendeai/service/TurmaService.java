package br.com.aprendeai.service;

import java.util.List;

import br.com.aprendeai.dtos.RequestCodigoTurmaDTO;
import br.com.aprendeai.dtos.TurmaCreateDto;
import br.com.aprendeai.dtos.TurmaResponseDto;
import br.com.aprendeai.dtos.TurmaUpdateDto;

public interface TurmaService {
	
	public TurmaResponseDto criarTurma(TurmaCreateDto dto);

	public List<TurmaResponseDto> listarTodas();

	public TurmaResponseDto buscarPorId(Long id);

	public TurmaResponseDto atualizar(Long id, TurmaUpdateDto dto);

	public void deletar(Long id);

	public TurmaResponseDto clonarTurma(Long id);

	public TurmaResponseDto adicionarAluno(RequestCodigoTurmaDTO codigo, Long alunoId);

	public TurmaResponseDto removerAluno(Long turmaId, Long alunoId);

}
