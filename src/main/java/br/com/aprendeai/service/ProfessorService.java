package br.com.aprendeai.service;

import java.util.List;

import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;

public interface ProfessorService {
	
	public UsuarioResponseDto criarProfessor(UsuarioCreateDto dto);
	
	public List<UsuarioResponseDto> listarProfessores();
	
	public UsuarioResponseDto buscarProfessorPorId(Long id);
	
	public UsuarioResponseDto atualizarProfessor(Long id, UsuarioUpdateDto dto);
	
	public void deletarProfessor(Long id);

}
