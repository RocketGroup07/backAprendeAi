package br.com.aprendeai.controller;

import br.com.aprendeai.dtos.AtividadeCreateDto;
import br.com.aprendeai.enums.ArquivoTipo;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.ArquivoService;
import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;                                                                             
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    public TelaAtividadesController(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository,
			TurmaRepository turmaRepository, ArquivoService arquivoService) {
		this.atividadeRepository = atividadeRepository;
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
		this.arquivoService = arquivoService;
	}

    @PostMapping(value = "/professor/criar/{turmaId}/{professorId}", consumes = {"multipart/form-data"})
//    public ResponseEntity<?> postarAtividadeComAnexo(
//            @PathVariable("turmaId") Long turmaId, 
//            @PathVariable("professorId") Long professorId, 
//            @ModelAttribute AtividadeCreateDto dto, 
//            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
//        try {
//            Optional<Turma> turmaOptional = turmaRepository.findById(turmaId);
//            Optional<Usuario> professorOptional = usuarioRepository.findById(professorId);
//
//            if (turmaOptional.isEmpty() || professorOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Turma ou Professor não encontrado.");
//            }
//
//            Turma turma = turmaOptional.get();
//            Usuario professor = professorOptional.get();
//
//            atividade.setTurma(turma);
//            atividade.setProfessor(professor);
//            atividade.setDataAtividade(LocalDateTime.now()); 
//
//            if (arquivo != null && !arquivo.isEmpty()) {
//                Arquivo arquivoAnexo = arquivoService.uploadArquivo(arquivo);
//                
//                // Define os campos do relacionamento
//                arquivoAnexo.setEnviadoPor(professor);
//                arquivoAnexo.setAtividade(atividade); 
//                arquivoAnexo.setTipo(ArquivoTipo.ANEXO);
//                
//                atividade.setArquivoAnexo(arquivoAnexo); 
//            }
//            
//            Atividade novaAtividade = atividadeRepository.save(atividade);
//            
//            return ResponseEntity.ok(novaAtividade);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro ao postar atividade: " + e.getMessage());
//        }
//    }
    
    
    @GetMapping("/{atividadeId}/download/anexo")
    public ResponseEntity<Resource> baixarArquivoAnexo(@PathVariable Long atividadeId) {
        try {
            Optional<Atividade> atividadeOptional = atividadeRepository.findById(atividadeId);
            
            if (atividadeOptional.isEmpty() || atividadeOptional.get().getArquivoAnexo() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Arquivo arquivoAnexo = atividadeOptional.get().getArquivoAnexo();
            Resource recurso = arquivoService.downloadArquivo(arquivoAnexo.getId());

            String contentType = arquivoAnexo.getTipoArquivo();
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivoAnexo.getNomeArquivo() + "\"")
                    .body(recurso);

        } catch (RuntimeException e) {
            // Lidar com falha no download ou arquivo não encontrado no serviço
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
	// Criar atividade
    @PostMapping("/criar/{turmaId}")
    public ResponseEntity<?> adicionarAtividade(@PathVariable ("turmaId") Long turmaId, @Valid @RequestBody Atividade atividade) {
        try {
        	Optional<Turma> turmaOptional = turmaRepository.findById(turmaId);
        	
        	if(turmaOptional.isEmpty()) {
        		return ResponseEntity.status(HttpStatus.NOT_FOUND)
        				.body("Turma não encontrada");
        	}
        	
        	Turma turma = turmaOptional.get();
        	
            atividade.setTurma(turma);
            
            Atividade novaAtividade = atividadeRepository.save(atividade);
            
            return ResponseEntity.ok(novaAtividade);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar atividade.");
        }
    }

    // Listar atividades
    @GetMapping("/{turmaId}")
    public ResponseEntity<?> listarAtividades(@PathVariable ("turmaId") Long turmaId) {
        try {
    
            List<Atividade> atividades = atividadeRepository.findByTurmaId(turmaId);

            if (atividades.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhuma atividade cadastrada.");
            }

            return ResponseEntity.ok(atividades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar atividades.");
        }
    }
    
    @PostMapping(value = "/{atividadeId}/entregar/arquivo/{alunoId}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> entregarTarefaComArquivo(
            @PathVariable("atividadeId") Long atividadeId,
            @PathVariable("alunoId") Long alunoId,
            @RequestPart("arquivo") MultipartFile arquivo) {
        try {
            if (arquivo.isEmpty()) {
                return ResponseEntity.badRequest().body("O arquivo de entrega não pode ser vazio.");
            }

            Optional<Atividade> atividadeOptional = atividadeRepository.findById(atividadeId);
            Optional<Usuario> alunoOptional = usuarioRepository.findById(alunoId);

            if (atividadeOptional.isEmpty() || alunoOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Atividade ou Aluno não encontrado.");
            }

            Atividade atividade = atividadeOptional.get();
            Usuario aluno = alunoOptional.get();

            Arquivo arquivoEntrega = arquivoService.uploadArquivo(arquivo);

            arquivoEntrega.setEnviadoPor(aluno);
            arquivoEntrega.setAtividade(atividade); 
            arquivoEntrega.setTipo(ArquivoTipo.ENTREGA);
            
            if (!atividade.isEntregue()) {
                atividade.setEntregue(true);
                atividadeRepository.save(atividade);
            }

            return ResponseEntity.ok(arquivoEntrega);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar arquivo de entrega: " + e.getMessage());
        }
    }

    // Buscar atividade por ID
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

    // Marcar atividade como entregue
    @PostMapping("/{id}/entregar")
    public ResponseEntity<?> entregarAtividade(@PathVariable("id") Long id) {
        try {
            Optional<Atividade> atividadeOptional = atividadeRepository.findById(id);

            if (atividadeOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada.");
            }

            Atividade atividade = atividadeOptional.get();
            if (atividade.isEntregue()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Atividade já foi entregue.");
            }

            atividade.setEntregue(true);
            atividadeRepository.save(atividade);

            return ResponseEntity.ok("Atividade marcada como entregue.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao entregar atividade.");
        }
    }
}
