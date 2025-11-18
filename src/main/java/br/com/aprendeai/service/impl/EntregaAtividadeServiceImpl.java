package br.com.aprendeai.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.EntregaAtividadeRequestDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.enums.ArquivoTipo;
import br.com.aprendeai.enums.StatusAtividade;
import br.com.aprendeai.mappers.AtividadeMapper;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.EntregaAtividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.ArquivoRepository;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.EntregaAtividadeRepository;
import br.com.aprendeai.service.ArquivoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

public class EntregaAtividadeServiceImpl {
	
	
	private final EntregaAtividadeRepository entregaAtividadeRepository;
	private final AtividadeMapper atividadeMapper;
	private final ObjectMapper objectMapper;
	private final ArquivoService arquivoServie;
	private final AccessControlService accessControlService;
	private final ArquivoRepository arquivoRepo;
	private final AtividadeRepository atividadeRepository;
	
	public EntregaAtividadeServiceImpl(EntregaAtividadeRepository entregaAtividadeRepository,
			AtividadeMapper atividadeMapper, ObjectMapper objectMapper, ArquivoService arquivoServie,
			AccessControlService accessControlService, ArquivoRepository arquivoRepo,
			AtividadeRepository atividadeRepository) {
		this.entregaAtividadeRepository = entregaAtividadeRepository;
		this.atividadeMapper = atividadeMapper;
		this.objectMapper = objectMapper;
		this.arquivoServie = arquivoServie;
		this.accessControlService = accessControlService;
		this.arquivoRepo = arquivoRepo;
		this.atividadeRepository = atividadeRepository;
	}

	@Override
    @Transactional
    public List<AtividadeResponseDto> listarAtividadesEntregues(Long turmaId){
    	List<EntregaAtividade> atividadesEntregues = entregaAtividadeRepository.findByAtividade_TurmaIdAndEntregueTrue(turmaId);
        
        Usuario usuario = accessControlService.getUsuarioLogado();
        
        // Supondo que você tem um método para buscar a Turma a partir do ID
        Turma turma = buscarTurmaId(turmaId); 
        
        if(!turma.getProfessor().equals(usuario)) {
            throw new IllegalArgumentException("Acesso negado.");
        }
       
        return atividadesEntregues.stream()
                .map(entregaAtividade -> atividadeMapper.toResponseDTO(entregaAtividade.getAtividade(), entregaAtividade))
                .collect(Collectors.toList());
    }
	
	@Override
    @Transactional
    public AtividadeResponseDto entregarAtividade(Long atividadeId, String resposta, MultipartFile arquivo) {
        try {
            Atividade atividade = buscarAtividadeId(atividadeId);
            Turma turma = atividade.getTurma();
            accessControlService.isAluno(turma);

            Usuario aluno = accessControlService.getUsuarioLogado();

            EntregaAtividade novaEntrega = new EntregaAtividade();
            novaEntrega.setAtividade(atividade);
            novaEntrega.setAluno(aluno);
            
            novaEntrega.entregar();
            
            EntregaAtividadeRequestDto dto = objectMapper.readValue(resposta, EntregaAtividadeRequestDto.class);
            if (dto.resposta() != null) {
                novaEntrega.setRespostaTexto(dto.resposta()); 
            }

            List<Arquivo> arquivosEntrega = new ArrayList<>();
            if (arquivo != null && !arquivo.isEmpty()) {
                Arquivo arquivoEntrega = arquivoService.uploadArquivo(arquivo);
                arquivoEntrega.setEntregaAtividade(novaEntrega); 
                arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
                arquivosEntrega.add(arquivoEntrega);
            }
            
            novaEntrega.setArquivosEntrega(arquivosEntrega);

            EntregaAtividade entregaSalva = entregaAtividadeRepository.save(novaEntrega); 
            return atividadeMapper.toResponseDTO(atividade); 

        } catch (Exception e) {
        	e.printStackTrace(); 
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Causa da exceção desconhecida. Verifique os logs do servidor.";
            throw new RuntimeException("Erro ao entregar atividade: " + errorMessage, e);
        }
    }
    
    @Override
    @Transactional
    public Arquivo baixarEntregaDeAluno(Long atividadeId, Long alunoId) {
    	
    	 EntregaAtividade atividade = buscarAtividadeId(atividadeId);
    	 
    	 Usuario usuarioLogado = accessControlService.getUsuarioLogado();         
         Turma turma = atividade.getTurma();
         
         boolean isProfessor = true;
         try {
             accessControlService.verificarAcessoProfessor(turma); 
         } catch (AccessDeniedException e) {
             isProfessor = false; 
         }
         
         boolean eOProprioAluno = usuarioLogado.getId().equals(alunoId);
         
         if (!isProfessor && !eOProprioAluno) {
             throw new AccessDeniedException("Você não tem permissão para baixar esta entrega.");
         }
         
         Arquivo arquivoEntrega = arquivoRepo
     	        .findByAtividadeIdAndEnviadoPorId(atividadeId, alunoId)
     	        .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada para esta atividade e aluno."));
         
         return arquivoEntrega;
         
    }
    
    @Override
    @Transactional
    public AtividadeResponseDto editarEntrega(Long atividadeId, String novaResposta, MultipartFile novoArquivo) {
        try {
            Atividade atividade = buscarAtividadeId(atividadeId);
            Turma turma = atividade.getTurma();
            accessControlService.isAluno(turma);

            Usuario aluno = accessControlService.getUsuarioLogado();
            
            if (atividade.getRespostaEnviadaPor() == null || !atividade.getRespostaEnviadaPor().getId().equals(aluno.getId())) {
                throw new RuntimeException("Você não pode editar a entrega de outro aluno.");
            }


            if (novoArquivo != null && !novoArquivo.isEmpty()) {
            	for (Arquivo arquivo : atividade.getArquivosEntrega()) {
            	    arquivoService.deletarArquivo(arquivo.getId());
            	}
            	atividade.setArquivosEntrega(Collections.emptyList());

                Arquivo arquivoEntrega = arquivoService.uploadArquivo(novoArquivo);
                arquivoEntrega.setEnviadoPor(aluno);
                arquivoEntrega.setAtividade(atividade);
                arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
                atividade.setArquivosEntrega(Arrays.asList(arquivoEntrega));
            }

            if (novaResposta != null) {
                atividade.setResposta(novaResposta);
            }

            atividadeRepository.save(atividade);
            return atividadeMapper.toResponseDTO(atividade);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar entrega: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void excluirEntrega(Long atividadeId) {
        try {
            Atividade atividade = buscarAtividadeId(atividadeId);
            Turma turma = atividade.getTurma();
            accessControlService.isAluno(turma);
            
            Usuario aluno = accessControlService.getUsuarioLogado();
            
            if (atividade.getRespostaEnviadaPor() == null || !atividade.getRespostaEnviadaPor().getId().equals(aluno.getId())) {
                throw new RuntimeException("Você não pode excluir a entrega de outro aluno.");
            }
            
            for (Arquivo arquivo : atividade.getArquivosEntrega()) {
        	    arquivoService.deletarArquivo(arquivo.getId());
        	}
            atividade.setArquivosEntrega(Collections.emptyList());
            atividade.setRespostaEnviadaPor(null);
            atividade.setResposta(null);

            atividade.setEntregue(false);
            atividade.setStatus(StatusAtividade.PENDENTE);

            atividadeRepository.save(atividade);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir entrega: " + e.getMessage(), e);
        }
    }


    @Override
    @Transactional
    public AtividadeResponseDto corrigirAtividade(Long atividadeId, RequestNotaDto dto) {
        Atividade atividade = buscarAtividadeId(atividadeId);
        Turma turma = atividade.getTurma();
        accessControlService.verificarAcessoProfessor(turma);

        if (atividade.getStatus() != StatusAtividade.ENTREGUE) {
            throw new IllegalStateException("A atividade só pode ser corrigida após ser entregue.");
        }

        atividade.corrigir(dto.nota(), dto.feedback());
        atividade.setStatus(StatusAtividade.CORRIGIDA);

        atividadeRepository.save(atividade);
        return atividadeMapper.toResponseDTO(atividade);
    }
    
    private Atividade buscarAtividadeId(Long id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com id: " + id));
    }

}
