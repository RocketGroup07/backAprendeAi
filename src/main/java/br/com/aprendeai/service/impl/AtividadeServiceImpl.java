package br.com.aprendeai.service.impl;

import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.util.AuthenticatedUser;

public class AtividadeServiceImpl {

	private final AtividadeRepository atividadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final ArquivoService arquivoService;
    private final AuthenticatedUser authenticatedUser;
	
    public AtividadeServiceImpl(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository,
			TurmaRepository turmaRepository, ArquivoService arquivoService, AuthenticatedUser authenticatedUser) {
		this.atividadeRepository = atividadeRepository;
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
		this.arquivoService = arquivoService;
		this.authenticatedUser = authenticatedUser;
	}
    
    
}
