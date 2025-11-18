package br.com.aprendeai.service.impl;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.AtividadeCreateDto;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeUpdateDto;
import br.com.aprendeai.enums.ArquivoTipo;
import br.com.aprendeai.mappers.AtividadeMapper;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.EntregaAtividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.AtividadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class AtividadeServiceImpl implements AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final TurmaRepository turmaRepository;
    private final ArquivoService arquivoService;
    private final AccessControlService accessControlService;
    private final AtividadeMapper atividadeMapper;
    private final ObjectMapper objectMapper;
//    private final EntregaAtividadeRepository entregaAtividadeRepo;

	public AtividadeServiceImpl(AtividadeRepository atividadeRepository, TurmaRepository turmaRepository,
			ArquivoService arquivoService, AccessControlService accessControlService, AtividadeMapper atividadeMapper,
			ObjectMapper objectMapper
//			
) {
		this.atividadeRepository = atividadeRepository;
		this.turmaRepository = turmaRepository;
		this.arquivoService = arquivoService;
		this.accessControlService = accessControlService;
		this.atividadeMapper = atividadeMapper;
		this.objectMapper = objectMapper;
//		this.entregaAtividadeRepo = entregaAtividadeRepo;
	}

	@Override
    @Transactional
    public AtividadeResponseDto criarAtividade(Long turmaId, String Atividade, MultipartFile arquivo) {
        try {
            AtividadeCreateDto dto = objectMapper.readValue(Atividade, AtividadeCreateDto.class);

            Turma turma = buscarTurmaId(turmaId);
            accessControlService.verificarAcessoProfessor(turma);

            Usuario professor = accessControlService.getUsuarioLogado();

            Atividade atividade = atividadeMapper.toEntityFromCreateDto(dto);
            atividade.setProfessor(professor);
            atividade.setDataAtividade(LocalDateTime.now());
            atividade.setTurma(turma);

            if (arquivo != null && !arquivo.isEmpty()) {
                Arquivo arquivoAnexo = arquivoService.uploadArquivo(arquivo);
                arquivoAnexo.setEnviadoPor(professor);
                arquivoAnexo.setAtividade(atividade);
                arquivoAnexo.setTipo(ArquivoTipo.ANEXO);
                atividade.setArquivoAnexo(Arrays.asList(arquivoAnexo));
            }

            Atividade salva = atividadeRepository.save(atividade);
            turma.getAtividades().add(salva);
            turmaRepository.save(turma);

            return atividadeMapper.toResponseDTO(salva);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar atividade: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public List<AtividadeResponseDto> listarAtividades(Long turmaId) {
        Turma turma = buscarTurmaId(turmaId);
        accessControlService.verificarParticipacao(turma);

        return atividadeRepository.findByTurma_Id(turmaId)
                .stream()
                .map(atividadeMapper::toResponseDTO)
                .toList();
    }
    
    @Override
    @Transactional
    public AtividadeResponseDto buscarPorId(Long atividadeId) {
    	Atividade atividade = buscarAtividadeId(atividadeId);
    	return atividadeMapper.toResponseDTO(atividade);
    }

    @Override
    @Transactional
    public Arquivo baixarAnexo(Long atividadeId) {
        Atividade atividade = buscarAtividadeId(atividadeId);
        
        Turma turma = atividade.getTurma();
        
        accessControlService.verificarParticipacao(turma);

        if (atividade.getArquivoAnexo() == null || atividade.getArquivoAnexo().isEmpty()) {
            throw new EntityNotFoundException("Nenhum anexo encontrado para esta atividade.");
        }

        Arquivo arquivo = atividade.getArquivoAnexo().get(0);
        return arquivo;
    }

    

    @Override
    @Transactional
    public AtividadeResponseDto atualizarAtividade(Long id, AtividadeUpdateDto dto) {
        Atividade atividade = buscarAtividadeId(id);
        
        accessControlService.verificarAcessoProfessor(atividade.getTurma());

        atividade.setTitulo(dto.titulo());
        atividade.setDataEntrega(dto.dataEntrega());
        atividade.setConteudo(dto.conteudo());

        atividadeRepository.save(atividade);
        return atividadeMapper.toResponseDTO(atividade);
    }

    @Override
    @Transactional
    public void deletarAtividade(Long id) {
        if (!atividadeRepository.existsById(id)) {
            throw new EntityNotFoundException("Atividade não encontrada.");
        }
        
//        entregaAtividadeRepo.deleteByAtividade(id);
        atividadeRepository.deleteById(id);
    }

    private Turma buscarTurmaId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
    }

    private Atividade buscarAtividadeId(Long id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com id: " + id));
    }
}
