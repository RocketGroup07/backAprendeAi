package br.com.aprendeai.controller;

import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/turmas")
@CrossOrigin
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/")
    public ResponseEntity<?> criarTurma(@Valid @RequestBody Turma turma) {
        try {
            if (turma.getNome() == null || turma.getNome().isEmpty() || turma.getProfessor() == null) {
                return ResponseEntity.badRequest().body("Preencha todos os campos obrigatórios.");
            }

            turma.setCodigo(gerarCodigoUnico());
            turma.setCriadoEm(LocalDateTime.now());

            Turma novaTurma = turmaRepository.save(turma);
            return ResponseEntity.ok(novaTurma);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro interno no sistema.");
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> listarTurmas() {
        try {
            List<Turma> turmas = turmaRepository.findAll();

            if (turmas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Nenhuma turma cadastrada.");
            }

            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar as turmas.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTurmaPorId(@PathVariable("id") Long id) {
        try {
            Optional<Turma> turma = turmaRepository.findById(id);

            if (turma.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhuma turma encontrada com o ID: " + id);
            }

            return ResponseEntity.ok(turma.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor.");
        }
    }

    @PutMapping("/{id}") 
    public ResponseEntity<?> atualizarTurma(@PathVariable("id") Long id, @RequestBody Turma turmaAtualizada) {
        try {
            Optional<Turma> turmaExistente = turmaRepository.findById(id);

            if (turmaExistente.isPresent()) {
                Turma turma = turmaExistente.get();
                turma.setNome(turmaAtualizada.getNome());
                turma.setLimiteAlunos(turmaAtualizada.getLimiteAlunos());
                turma.setProfessor(turmaAtualizada.getProfessor());
                turma.setAlunos(turmaAtualizada.getAlunos());

                turmaRepository.save(turma);
                return ResponseEntity.ok(turma);

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Turma não encontrada.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar a turma.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTurma(@PathVariable("id") Long id) {
        try {
            if (turmaRepository.existsById(id)) {
                turmaRepository.deleteById(id);
                return ResponseEntity.ok("Turma deletada com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Turma não encontrada.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor.");
        }
    }

    @PostMapping("/{id}/clonar")
    public ResponseEntity<?> clonarTurma(@PathVariable("id") Long id) {
        try {
            Optional<Turma> turmaExistente = turmaRepository.findById(id);

            if (turmaExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Turma não encontrada para clonagem.");
            }

            Turma original = turmaExistente.get();
            Turma clone = new Turma();
            clone.setNome(original.getNome() + " (Cópia)");
            clone.setLimiteAlunos(0);
            clone.setProfessor(original.getProfessor());
            clone.setCodigo(gerarCodigoUnico());
            clone.setCriadoEm(LocalDateTime.now());
            clone.setAlunos(new HashSet<>()); // sem alunos

            Turma turmaClonada = turmaRepository.save(clone);

            return ResponseEntity.ok(turmaClonada);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao clonar a turma.");
        }
    }

	@PostMapping("/{codigo}/adicionar-aluno/{alunoId}")
    public ResponseEntity<?> adicionarAluno(@PathVariable("codigo") String codigo, @PathVariable("alunoId") Long alunoId) {
        try {
        	Optional<Turma> turmaOptional = turmaRepository.findByCodigo(codigo);

            if (turmaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Código de turma inválido.");
            }

            Optional<Usuario> alunoOptional = usuarioRepository.findById(alunoId);
            if (alunoOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aluno não encontrado.");
            }

            Turma turma = turmaOptional.get();
            Usuario aluno = alunoOptional.get();
            
            if(turma.getQtdAlunos() < turma.getLimiteAlunos()) {
            	turma.getAlunos().add(aluno);
            }else {
            	return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            			.body("A quantidade de alunos nessa turma já está completa.");
            }

            
            turma.getQtdAlunos();

            turmaRepository.save(turma);

            return ResponseEntity.ok("Aluno adicionado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar aluno na turma.");
        }
    }
    
    @DeleteMapping("/{id}/remover-aluno/{alunoId}")
    public ResponseEntity<?> removerAluno(@PathVariable("id") Long id, @PathVariable("alunoId") Long alunoId) {
        try {
            Optional<Turma> turmaOptional = turmaRepository.findById(id);

            if (turmaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Turma não encontrada.");
            }

            Optional<Usuario> alunoOptional = usuarioRepository.findById(alunoId);
            if (alunoOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aluno não encontrado.");
            }

            Turma turma = turmaOptional.get();
            Usuario aluno = alunoOptional.get();

            if (!turma.getAlunos().contains(aluno)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Aluno não está nesta turma.");
            }

            turma.getAlunos().remove(aluno);
            turma.getQtdAlunos();

            turmaRepository.save(turma);

            return ResponseEntity.ok("Aluno removido com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover aluno da turma.");
        }
    }

    private String gerarCodigoUnico() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String codigo;

        do {
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            codigo = sb.toString();
        } while (turmaRepository.existsByCodigo(codigo));

        return codigo;
    }
}