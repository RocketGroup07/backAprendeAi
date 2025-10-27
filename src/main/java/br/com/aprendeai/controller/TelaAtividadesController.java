package br.com.aprendeai.controller;

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
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.util.AuthenticatedUser;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;                                                                             
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/atividades")
@CrossOrigin

public class TelaAtividadesController {

    private final AtividadeRepository atividadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final ArquivoService arquivoService;
    private final AuthenticatedUser authenticatedUser;
    private final AtividadeMapper atividadeMapper;
    private final ObjectMapper objectMapper;
    
    public TelaAtividadesController(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository,
			TurmaRepository turmaRepository, ArquivoService arquivoService, AuthenticatedUser authenticatedUser,
			AtividadeMapper atividadeMapper) {
		this.atividadeRepository = atividadeRepository;
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
		this.arquivoService = arquivoService;
		this.authenticatedUser = authenticatedUser;
		this.atividadeMapper = atividadeMapper;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

    @PostMapping(value = "/criar/{turmaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AtividadeResponseDto criarAtividade(
            @PathVariable Long turmaId,
           @RequestPart(value = "atividade") String Atividade,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
    	
    	AtividadeCreateDto dto = null;
    	
    	try {
			 dto = objectMapper.readValue(Atividade, AtividadeCreateDto.class);
		} catch (Exception e) {
			System.out.println(e);
		}
 
        Usuario usuario = authenticatedUser.getCurrentUser();
 
        Turma turma = buscarTurmaId(turmaId);

        if (!turma.getProfessor().getId().equals(usuario.getId())) {
        	throw new RuntimeException("Apenas o professor desta turma pode realizar a operação.");
        }
 
        try {
            Atividade atividade = atividadeMapper.toEntityFromCreateDto(dto);
            atividade.setProfessor(usuario);
            atividade.setEntregue(false);
            atividade.setDataAtividade(LocalDateTime.now());
            atividade.setTurma(turma);
 
            if (arquivo != null && !arquivo.isEmpty()) {
                Arquivo arquivoAnexo = arquivoService.uploadArquivo(arquivo);
                arquivoAnexo.setEnviadoPor(usuario);
                arquivoAnexo.setAtividade(atividade);
                arquivoAnexo.setTipo(ArquivoTipo.ANEXO);
                atividade.setArquivoAnexo(Arrays.asList(arquivoAnexo));
            }
 
            Atividade salva = atividadeRepository.save(atividade);
            turma.getAtividades().add(salva);
            
            turmaRepository.save(turma);
           
            return atividadeMapper.toResponseDTO(salva);
 
        } catch (Exception e) {
        	throw new RuntimeException("Ocorreu um erro interno: " + e);
        }
    }
    
	@GetMapping("/{atividadeId}/download/anexo")
    public ResponseEntity<Resource> baixarArquivoAnexo(@PathVariable Long atividadeId) {
        try {
            Optional<Atividade> atividadeOptional = atividadeRepository.findById(atividadeId);
            
            if (atividadeOptional.isEmpty() || atividadeOptional.get().getArquivoAnexo() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Arquivo arquivoAnexo = atividadeOptional.get().getArquivoAnexo().get(0);
            Resource recurso = arquivoService.downloadArquivo(arquivoAnexo.getId());

            String contentType = arquivoAnexo.getTipoArquivo();
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoAnexo.getNomeArquivo() + "\"")
                    .body(recurso);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Listar atividades
    @GetMapping("/turma/{turmaId}")
    public List<AtividadeResponseDto> listarAtividades(@PathVariable Long turmaId) {
    	Usuario usuario = authenticatedUser.getCurrentUser();
    	
    	Optional<Turma> turma = turmaRepository.findById(turmaId);
    	
    	Turma turmaBuscada = turma.get();
    	
    	if(!turmaBuscada.getAlunos().contains(usuario)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. O usuário não pertence a esta turma.");
    	}
    	
    	List<Atividade> atividades = atividadeRepository.findByTurma_Id(turmaId);
    	return atividades.stream()
    			.map(atividadeMapper::toResponseDTO)
    			.toList();
    }
    
    @PostMapping(value = "/{atividadeId}/entregar/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> entregarAtividade(
            @PathVariable("atividadeId") Long atividadeId,
            @PathVariable("alunoId") Long alunoId,
            @RequestPart(value = "resposta") String resposta,
            @RequestPart("arquivo") MultipartFile arquivo) {
        try {
        	
        	Usuario usuario = authenticatedUser.getCurrentUser();
        	 
            Atividade atividade = buscarAtividadeId(atividadeId);
            
            boolean pertenceATurma = atividade.getTurma()
            		.getAlunos()
            		.stream()
            		.anyMatch(aluno -> aluno.getId().equals(usuario.getId()));
            
            if(!pertenceATurma) {
            	throw new RuntimeException("Apenas alunos dessa turma podem entregar atividades.");
            }
        	
        	AtividadeSubmitRequestDto dto = null;
        	
        	dto = objectMapper.readValue(resposta, AtividadeSubmitRequestDto.class);
        	
        	if (arquivo != null && !arquivo.isEmpty()) {
        		Arquivo arquivoEntrega = arquivoService.uploadArquivo(arquivo);
        		arquivoEntrega.setEnviadoPor(usuario);
        		arquivoEntrega.setAtividade(atividade);
        		arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
        		atividade.setArquivosEntrega(Arrays.asList(arquivoEntrega));
        	}
        	
        	if(resposta != null) {
        		atividade.setResposta(resposta);
        	}
        	
        	atividade.setEntregue(true);
        	atividade.setStatus(StatusAtividade.ENTREGUE);

            return ResponseEntity.ok(atividadeMapper.toResponseDTO(atividade));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar arquivo de entrega: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        try {
            Optional<Atividade> atividadeOptional = atividadeRepository.findById(id);

            if (atividadeOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada.");
            }

            return ResponseEntity.ok(atividadeOptional.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar atividade.");
        }
    }
    
    @PutMapping("/{atividadeId}/corrigir")
    public ResponseEntity<?> corrigirAtividade(@PathVariable("atividadeId") Long atividadeId, @RequestBody RequestNotaDto dto){
    	
    	try {
    		Usuario usuario = authenticatedUser.getCurrentUser();
    		
			Atividade atividade = buscarAtividadeId(atividadeId);
			
			if(!atividade.getTurma().getProfessor().getId().equals(usuario.getId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("Apenas o professor desta turma pode realizar esta operação.");
			}
			
			if(atividade.getStatus() != StatusAtividade.ENTREGUE) {
				return ResponseEntity.badRequest()
						.body("A atividade só pode ser corrigida após ser entregue");
			}
			
			atividade.corrigir(dto.nota(), dto.feedback());
			atividade.setStatus(StatusAtividade.CORRIGIDA);
			atividadeRepository.save(atividade);
			
			return ResponseEntity.ok(atividadeMapper.toResponseDTO(atividade));
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao corrigir atividade: " + e.getMessage());
		}
    }

    // Atualizar atividade
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAtividade(@PathVariable("id") Long id,
                                                @RequestBody Atividade atividadeAtualizada) {
        try {
            Optional<Atividade> atividadeOptional = atividadeRepository.findById(id);

            if (atividadeOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada.");
            }

            Atividade atividade = atividadeOptional.get();
            atividade.setTitulo(atividadeAtualizada.getTitulo());
            atividade.setDataAtividade(atividadeAtualizada.getDataAtividade());
            atividade.setDataEntrega(atividadeAtualizada.getDataEntrega());
            atividade.setEntregue(atividadeAtualizada.isEntregue());
            atividade.setConteudo(atividadeAtualizada.getConteudo());

            atividadeRepository.save(atividade);

            return ResponseEntity.ok(atividade);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar atividade.");
        }
    }

    // Deletar atividade
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAtividade(@PathVariable("id") Long id) {
        try {
            if (atividadeRepository.existsById(id)) {
                atividadeRepository.deleteById(id);
                return ResponseEntity.ok("Atividade deletada com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar atividade.");
        }
    }

    private Turma buscarTurmaId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
    }
    
    private Atividade buscarAtividadeId(Long id) {
    	return atividadeRepository.findById(id)
    			.orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com id " +id));
    }
}
