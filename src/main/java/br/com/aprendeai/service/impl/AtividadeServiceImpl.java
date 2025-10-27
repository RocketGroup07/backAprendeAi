package br.com.aprendeai.service.impl;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.AtividadeCreateDto;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeSubmitRequestDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.enums.ArquivoTipo;
import br.com.aprendeai.enums.StatusAtividade;
import br.com.aprendeai.mappers.AtividadeMapper;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.AtividadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
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

    public AtividadeServiceImpl(
            AtividadeRepository atividadeRepository,
            TurmaRepository turmaRepository,
            ArquivoService arquivoService,
            AccessControlService accessControlService,
            AtividadeMapper atividadeMapper) {

        this.atividadeRepository = atividadeRepository;
        this.turmaRepository = turmaRepository;
        this.arquivoService = arquivoService;
        this.accessControlService = accessControlService;
        this.atividadeMapper = atividadeMapper;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public AtividadeResponseDto criarAtividade(Long turmaId, String Atividade, MultipartFile arquivo) {
        try {
            AtividadeCreateDto dto = objectMapper.readValue(Atividade, AtividadeCreateDto.class);

            Turma turma = buscarTurmaId(turmaId);
            accessControlService.verificarAcessoProfessor(turma);

            Usuario professor = accessControlService.getUsuarioLogado();

            Atividade atividade = atividadeMapper.toEntityFromCreateDto(dto);
            atividade.setProfessor(professor);
            atividade.setEntregue(false);
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

    public List<AtividadeResponseDto> listarAtividades(Long turmaId) {
        Turma turma = buscarTurmaId(turmaId);
        accessControlService.verificarParticipacao(turma);

        return atividadeRepository.findByTurma_Id(turmaId)
                .stream()
                .map(atividadeMapper::toResponseDTO)
                .toList();
    }

    public Resource baixarAnexo(Long atividadeId) {
        Atividade atividade = buscarAtividadeId(atividadeId);

        if (atividade.getArquivoAnexo() == null || atividade.getArquivoAnexo().isEmpty()) {
            throw new EntityNotFoundException("Nenhum anexo encontrado para esta atividade.");
        }

        Arquivo arquivo = atividade.getArquivoAnexo().get(0);
        return arquivoService.downloadArquivo(arquivo.getId());
    }

    public AtividadeResponseDto entregarAtividade(Long atividadeId, String respostaJson, MultipartFile arquivo) {
        try {
            Atividade atividade = buscarAtividadeId(atividadeId);
            Turma turma = atividade.getTurma();
            accessControlService.isAluno(turma);

            Usuario aluno = accessControlService.getUsuarioLogado();

            AtividadeSubmitRequestDto dto = objectMapper.readValue(respostaJson, AtividadeSubmitRequestDto.class);

            if (arquivo != null && !arquivo.isEmpty()) {
                Arquivo arquivoEntrega = arquivoService.uploadArquivo(arquivo);
                arquivoEntrega.setEnviadoPor(aluno);
                arquivoEntrega.setAtividade(atividade);
                arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
                atividade.setArquivosEntrega(Arrays.asList(arquivoEntrega));
            }

            if (dto.resposta() != null) {
                atividade.setResposta(dto.resposta());
            }

            atividade.setEntregue(true);
            atividade.setStatus(StatusAtividade.ENTREGUE);

            atividadeRepository.save(atividade);
            return atividadeMapper.toResponseDTO(atividade);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao entregar atividade: " + e.getMessage(), e);
        }
    }

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

    public AtividadeResponseDto atualizarAtividade(Long id, Atividade atividadeAtualizada) {
        Atividade atividade = buscarAtividadeId(id);

        atividade.setTitulo(atividadeAtualizada.getTitulo());
        atividade.setDataAtividade(atividadeAtualizada.getDataAtividade());
        atividade.setDataEntrega(atividadeAtualizada.getDataEntrega());
        atividade.setEntregue(atividadeAtualizada.isEntregue());
        atividade.setConteudo(atividadeAtualizada.getConteudo());

        atividadeRepository.save(atividade);
        return atividadeMapper.toResponseDTO(atividade);
    }

    public void deletarAtividade(Long id) {
        if (!atividadeRepository.existsById(id)) {
            throw new EntityNotFoundException("Atividade não encontrada.");
        }
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
