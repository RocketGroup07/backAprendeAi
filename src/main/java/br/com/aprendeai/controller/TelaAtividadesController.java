package br.com.aprendeai.controller;

import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/atividades")
@CrossOrigin
public class TelaAtividadesController {

    private AtividadeRepository atividadeRepository;
    private UsuarioRepository usuarioRepository;
    private TurmaRepository turmaRepository;

    public TelaAtividadesController(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository, TurmaRepository turmaRepository) {
        this.atividadeRepository = atividadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.turmaRepository = turmaRepository;
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
