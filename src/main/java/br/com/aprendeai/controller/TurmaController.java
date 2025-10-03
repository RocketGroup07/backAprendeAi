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
		turmaService.clonarTurma(id);
		return ResponseEntity.ok("Turma clonada");
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


//    @Autowired
//    private TurmaRepository turmaRepository;
//
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//    @PostMapping("/")
//    public ResponseEntity<?> criarTurma(@Valid @RequestBody TurmaCreateDto dto) {
//        try {
//            Usuario professor = usuarioRepository.findById(dto.professorId())
//                    .orElseThrow(() -> new RuntimeException("Professor não encontrado."));
//
//            Turma turma = new Turma();
//            turma.setNome(dto.nome());
//            turma.setLimiteAlunos(dto.limiteAlunos());
//            turma.setProfessor(professor);
//            turma.setCodigo(gerarCodigoUnico());
//            turma.setCriadoEm(LocalDateTime.now());
//
//            Turma novaTurma = turmaRepository.save(turma);
//
//            return ResponseEntity.ok(TurmaResponseDto.fromEntity(novaTurma));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro ao criar a turma.");
//        }
//    }
//
//    @PostMapping("/validar-codigo")
//    public ResponseEntity<?> validarCodigoTurma(@RequestBody RequestCodigoTurmaDTO codigoTurma) {
//        Optional<Turma> turmaBuscada = turmaRepository.findByCodigo(codigoTurma.codigoTurma());
//
//        if (turmaBuscada.isPresent()) {
//            return ResponseEntity.ok().body("A turma existe!");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<?> listarTurmas() {
//        List<Turma> turmas = turmaRepository.findAll();
//
//        if (turmas.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                    .body("Nenhuma turma cadastrada.");
//        }
//
//        List<TurmaResponseDto> dtos = turmas.stream()
//                .map(TurmaResponseDto::fromEntity)
//                .toList();
//
//        return ResponseEntity.ok(dtos);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> buscarTurmaPorId(@PathVariable("id") Long id) {
//        return turmaRepository.findById(id)
//                .map(t -> ResponseEntity.ok(TurmaResponseDto.fromEntity(t)))
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Nenhuma turma encontrada com o ID: " + id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> atualizarTurma(@PathVariable("id") Long id, @RequestBody TurmaUpdateDto dto) {
//        try {
//            Optional<Turma> turmaExistente = turmaRepository.findById(id);
//
//            if (turmaExistente.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Turma não encontrada.");
//            }
//
//            Turma turma = turmaExistente.get();
//
//            if (dto.nome() != null) turma.setNome(dto.nome());
//            if (dto.limiteAlunos() != null) turma.setLimiteAlunos(dto.limiteAlunos());
//            if (dto.professorId() != null) {
//                Usuario professor = usuarioRepository.findById(dto.professorId())
//                        .orElseThrow(() -> new RuntimeException("Professor não encontrado."));
//                turma.setProfessor(professor);
//            }
//
//            Turma turmaAtualizada = turmaRepository.save(turma);
//
//            return ResponseEntity.ok(TurmaResponseDto.fromEntity(turmaAtualizada));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro interno ao atualizar a turma.");
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deletarTurma(@PathVariable("id") Long id) {
//        try {
//            if (turmaRepository.existsById(id)) {
//                turmaRepository.deleteById(id);
//                return ResponseEntity.ok("Turma deletada com sucesso!");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Turma não encontrada.");
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro interno no servidor.");
//        }
//    }
//
//    @PostMapping("/{id}/clonar")
//    public ResponseEntity<?> clonarTurma(@PathVariable("id") Long id) {
//        try {
//            Optional<Turma> turmaExistente = turmaRepository.findById(id);
//
//            if (turmaExistente.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Turma não encontrada para clonagem.");
//            }
//
//            Turma original = turmaExistente.get();
//            Turma clone = new Turma();
//            clone.setNome(original.getNome() + " (Cópia)");
//            clone.setLimiteAlunos(0);
//            clone.setProfessor(original.getProfessor());
//            clone.setCodigo(gerarCodigoUnico());
//            clone.setCriadoEm(LocalDateTime.now());
//            clone.setAlunos(new HashSet<>());
//
//            Turma turmaClonada = turmaRepository.save(clone);
//
//            return ResponseEntity.ok(TurmaResponseDto.fromEntity(turmaClonada));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro ao clonar a turma.");
//        }
//    }
//
//    @PostMapping("/{codigo}/adicionar-aluno/{alunoId}")
//    public ResponseEntity<?> adicionarAluno(@PathVariable("codigo") String codigo, @PathVariable("alunoId") Long alunoId) {
//        try {
//            Optional<Turma> turmaOptional = turmaRepository.findByCodigo(codigo);
//            if (turmaOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de turma inválido.");
//            }
//
//            Optional<Usuario> alunoOptional = usuarioRepository.findById(alunoId);
//            if (alunoOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado.");
//            }
//
//            Turma turma = turmaOptional.get();
//            Usuario aluno = alunoOptional.get();
//
//            if (turma.getAlunos().size() >= turma.getLimiteAlunos()) {
//                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
//                        .body("A quantidade de alunos nessa turma já está completa.");
//            }
//
//            turma.getAlunos().add(aluno);
//            turmaRepository.save(turma);
//
//            return ResponseEntity.ok(TurmaResponseDto.fromEntity(turma));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro ao adicionar aluno na turma.");
//        }
//    }
//
//    @DeleteMapping("/{id}/remover-aluno/{alunoId}")
//    public ResponseEntity<?> removerAluno(@PathVariable("id") Long id, @PathVariable("alunoId") Long alunoId) {
//        try {
//            Optional<Turma> turmaOptional = turmaRepository.findById(id);
//            if (turmaOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turma não encontrada.");
//            }
//
//            Optional<Usuario> alunoOptional = usuarioRepository.findById(alunoId);
//            if (alunoOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado.");
//            }
//
//            Turma turma = turmaOptional.get();
//            Usuario aluno = alunoOptional.get();
//
//            if (!turma.getAlunos().contains(aluno)) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aluno não está nesta turma.");
//            }
//
//            turma.getAlunos().remove(aluno);
//            turmaRepository.save(turma);
//
//            return ResponseEntity.ok(TurmaResponseDto.fromEntity(turma));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro ao remover aluno da turma.");
//        }
//    }
//
//    private String gerarCodigoUnico() {
//        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        SecureRandom random = new SecureRandom();
//        String codigo;
//
//        do {
//            StringBuilder sb = new StringBuilder(8);
//            for (int i = 0; i < 8; i++) {
//                sb.append(chars.charAt(random.nextInt(chars.length())));
//            }
//            codigo = sb.toString();
//        } while (turmaRepository.existsByCodigo(codigo));
//
//        return codigo;
//    }
//}
