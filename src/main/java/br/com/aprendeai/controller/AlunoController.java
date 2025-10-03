package br.com.aprendeai.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.RequestCodigoTurmaDTO;
import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.service.AlunoService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/alunos")
public class AlunoController {

    private AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
		this.alunoService = alunoService;
	}

	@PostMapping("/cadastro-com-turma")
    public ResponseEntity<?> cadastrarComTurma(@Valid @RequestBody UsuarioCreateDto dto,
                                               @RequestParam String codigoTurma) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alunoService.cadastrarAlunoComTurma(dto, codigoTurma));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> criarAluno(@Valid @RequestBody UsuarioCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alunoService.criarAluno(dto));
    }
    
    @PostMapping("/entrar-turma")
    public ResponseEntity<?> entrarEmTurma(@RequestBody RequestCodigoTurmaDTO codigoTurma){
    	return ResponseEntity.status(HttpStatus.CREATED)
    			.body(alunoService.entrarEmTurma(codigoTurma.codigoTurma()));
    }

    @GetMapping("/")
    public ResponseEntity<?> listarAlunos() {
        return ResponseEntity.ok(alunoService.listarAlunos());
    }
    
    @GetMapping("/minhas-turmas")
    public ResponseEntity<?> listarMinhasTurmas() {
        try {
            List<Turma> turmas = alunoService.encontrarTurmasDoAluno();
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAluno(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarAlunoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDto dto) {
        return ResponseEntity.ok(alunoService.atualizarAluno(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.ok("Aluno deletado com sucesso!");
    }
}
