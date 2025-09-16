package br.com.aprendeai.service.impl;

import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;

public class TurmaServiceImpl {
	
	private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    
	public TurmaServiceImpl(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository) {
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
	}
	
	

}
