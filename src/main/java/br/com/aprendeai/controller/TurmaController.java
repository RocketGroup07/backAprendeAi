package br.com.aprendeai.controller;

import br.com.aprendeai.dtos.*;
import br.com.aprendeai.service.TurmaService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turmas")
@CrossOrigin
public class TurmaController {
	
	private TurmaService turmaService;

	public TurmaController(TurmaService turmaService) {
		this.turmaService = turmaService;
	}
	
	@PostMapping("/")
	public ResponseEntity<?> criarTurma(@Valid @RequestBody TurmaCreateDto dto){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(turmaService.criarTurma(dto));
	}
	
	@PostMapping("/validar-codigo")
	public ResponseEntity<?> validarCodigoTurma(@RequestBody RequestCodigoTurmaDTO codigoTurma){
		return ResponseEntity.ok(turmaService.validarCodigo(codigoTurma));
	}
	
	@GetMapping("/")
	public ResponseEntity<?> listarTodas(){
		System.out.println("Passou no listar todas...");
		
		return ResponseEntity.ok(turmaService.listarTodas());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id){
		return ResponseEntity.ok(turmaService.buscarPorId(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody TurmaUpdateDto dto){
		return ResponseEntity.ok(turmaService.atualizar(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id){
		turmaService.deletar(id);
		return ResponseEntity.ok("Turma deletada");
	}
	
	@PostMapping("/{id}/clonar")
	public ResponseEntity<?> clonar(@PathVariable Long id){
		TurmaResponseDto turmaClonada = turmaService.clonarTurma(id);
	    return ResponseEntity.status(HttpStatus.CREATED).body(turmaClonada);
	}
	
	@PostMapping("/{codigo}/adicionar-aluno/{alunoId}")
	public ResponseEntity<?> adicionarAluno(@PathVariable ("codigo") RequestCodigoTurmaDTO codigo, @PathVariable("alunoId") Long alunoId){
		turmaService.adicionarAluno(codigo, alunoId);
		return ResponseEntity.ok("Aluno adicionado com sucesso!");
	
	}
	
	 @DeleteMapping("/{id}/remover-aluno/{alunoId}")
	public ResponseEntity<?> removerAluno(@PathVariable("id") Long id, @PathVariable("alunoId") Long alunoId){
		turmaService.removerAluno(id, alunoId);
		return ResponseEntity.ok("Aluno removido com sucesso!");
	}
	
}