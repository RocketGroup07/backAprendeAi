package br.com.aprendeai.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.service.AlunoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
@CrossOrigin
public class AlunoController {

	@Autowired
    private AlunoService alunoService;

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

    @GetMapping("/")
    public ResponseEntity<?> listarAlunos() {
        return ResponseEntity.ok(alunoService.listarAlunos());
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
