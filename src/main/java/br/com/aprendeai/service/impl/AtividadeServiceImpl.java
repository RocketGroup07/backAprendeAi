package br.com.aprendeai.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.aprendeai.mappers.AtividadeMapper;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.util.AuthenticatedUser;
import jakarta.persistence.EntityNotFoundException;

public class AtividadeServiceImpl {

	private final AtividadeRepository atividadeRepository;
    private final TurmaRepository turmaRepository;
    private final ArquivoService arquivoService;
    private final AuthenticatedUser authenticatedUser;
    private final AtividadeMapper atividadeMapper;
    private final ObjectMapper objectMapper;

    public AtividadeServiceImpl(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository,
			TurmaRepository turmaRepository, ArquivoService arquivoService, AuthenticatedUser authenticatedUser,
			AtividadeMapper atividadeMapper) {
		this.atividadeRepository = atividadeRepository;
		this.turmaRepository = turmaRepository;
		this.arquivoService = arquivoService;
		this.authenticatedUser = authenticatedUser;
		this.atividadeMapper = atividadeMapper;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	 
	
	
    
	private Turma buscarTurmaId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
    }
	
	private Atividade buscarPorId(Long id) {
		return atividadeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com id: " + id));
	}
}

