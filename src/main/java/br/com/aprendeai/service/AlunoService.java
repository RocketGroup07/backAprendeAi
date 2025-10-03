package br.com.aprendeai.service;

import java.util.List;

import br.com.aprendeai.dtos.LoginResponseDto;
import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.model.Turma;

public interface AlunoService {

	public LoginResponseDto cadastrarAlunoComTurma(UsuarioCreateDto dto, String codigoTurma);
	
	public UsuarioResponseDto criarAluno(UsuarioCreateDto dto);
	
	public String entrarEmTurma(String codigoTurma);
	
	public List<UsuarioResponseDto> listarAlunos();
	
	public UsuarioResponseDto buscarAlunoPorId(Long id);
	
	public UsuarioResponseDto atualizarAluno(Long id, UsuarioUpdateDto dto);
	
	public void deletarAluno(Long id);
	
	public List<Turma> encontrarTurmasDoAluno();
}
