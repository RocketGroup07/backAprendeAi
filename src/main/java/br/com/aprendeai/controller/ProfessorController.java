package br.com.aprendeai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.service.ProfessorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/professores")
@CrossOrigin
public class ProfessorController {
	
	private ProfessorService professorService;
	
	public ProfessorController(ProfessorService professorService) {
		this.professorService = professorService;
	}

	@PostMapping("/cadastrar")
    public ResponseEntity<?> criarAluno(@Valid @RequestBody UsuarioCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professorService.criarProfessor(dto));
    }

    @GetMapping("/")
    public ResponseEntity<?> listarAlunos() {
        return ResponseEntity.ok(professorService.listarProfessores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAluno(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarProfessorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDto dto) {
        return ResponseEntity.ok(professorService.atualizarProfessor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAluno(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.ok("Aluno deletado com sucesso!");
    }
}
