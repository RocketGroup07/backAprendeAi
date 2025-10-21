package br.com.aprendeai.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.aprendeai.dtos.AtividadeCreateDto;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeUpdateDto;
import br.com.aprendeai.enums.ArquivoTipo;
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

	public AtividadeResponseDto criarAtividade(Long turmaId, String Atividade, MultipartFile arquivo) {
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
	
	public ResponseEntity<Resource> baixarArquivoAnexo(Long atividadeId) {
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
	}
	
	public List<AtividadeResponseDto> listarAtividadesDaTurma(Long turmaId){
		Turma turma = buscarTurmaId(turmaId);
		
		List<Atividade> atividades = turma.getAtividades();
		
		return atividades
				.stream()
				.map(atividadeMapper::toResponseDTO)
				.toList();
		
	}
	
	public AtividadeResponseDto buscarAtividadePorId(Long id) {
		Atividade atividade = buscarPorId(id);
		return atividadeMapper.toResponseDTO(atividade);
	}
	
//	public AtividadeResponseDto atualizarAtividade(Long id, AtividadeUpdateDto dto) {
//		Atividade atividade = buscarPorId(id);
//		
//		Usuario usuario = authenticatedUser.getCurrentUser();
//	   
//	    if (!atividade.getProfessor().getId().equals(usuario.getId())) {
//	        throw new RuntimeException("Apenas o professor criador pode atualizar a atividade.");
//	    }
//		
//		atividade.setTitulo(dto.titulo());
//		atividade.setConteudo(dto.conteudo());
//		atividade.setDataEntrega(dto.dataEntrega());
//		
//		if(dto.arquivosAnexosIds() != null) {
//			
//		}
//		
//		
//	}
	
//	public Atividade atualizar(AtualizarAtividadeDTO dto) {
//    
//    // 1. Encontrar a Atividade
//    Atividade atividade = atividadeRepository.findById(dto.id())
//                                          .orElseThrow(() -> new NotFoundException("Atividade não encontrada"));
//    
//    // 2. Atualizar Campos Simples (pode usar um mapper ou fazer manualmente)
//    atividade.setTitulo(dto.titulo());
//    atividade.setConteudo(dto.conteudo());
//    // ... outros campos
//    
//    // 3. Atualizar Relação (Arquivos)
//    if (dto.arquivosAnexosIds() != null) {
//        // Encontra todos os objetos Arquivo com os IDs fornecidos
//        List<Arquivo> novosAnexos = arquivoRepository.findAllById(dto.arquivosAnexosIds());
//        
//        // Substitui a lista atual. Por ser @ManyToMany, a persistência gerencia as tabelas de associação.
//        atividade.setArquivoAnexo(novosAnexos);
//    }
//    
//    // 4. Salvar
//    return atividadeRepository.save(atividade);
//}
	
    
	private Turma buscarTurmaId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
    }
	
	private Atividade buscarPorId(Long id) {
		return atividadeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com id: " + id));
	}
}

