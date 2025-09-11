package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.mappers.UsuarioMapper;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos") 
@CrossOrigin
public class AlunoController {
	
	
	@Autowired
	private UsuarioRepository userRep;
	
	@Autowired
	private TurmaRepository turmaRepo;
	
	@Autowired
	private UsuarioMapper usuarioMapper;
	
	@PostMapping("/cadastro-com-turma")
	public ResponseEntity<?> cadastrarComTurma(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto, 
	                                           @RequestParam String codigoTurma) {
	    try {
	        // 1. Validação da turma
	        Optional<Turma> turmaOptional = turmaRepo.findByCodigo(codigoTurma);
	        if (turmaOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Código de turma inválido.");
	        }

	        Turma turma = turmaOptional.get();
	        
	        // 2. Validação de email/login único
	        if (userRep.existsByLogin(usuarioCreateDto.login())) {
	            return ResponseEntity.badRequest()
	                    .body("O email inserido já está em uso.");
	        }
	        
	        // 3. Converter DTO para Entity (o mapper já faz a validação básica e criptografia)
	        Usuario novoAluno = usuarioMapper.toEntityFromCreateDto(usuarioCreateDto);
	        
	        // 4. Configurar dados adicionais
	        novoAluno.setPapel(PapelEnum.USER);
	        novoAluno.setCriadoEm(LocalDateTime.now());
	        
	        // 5. Salvar aluno
	        Usuario alunoSalvo = userRep.save(novoAluno);

	        // 6. Adicionar aluno à turma
	        turma.getAlunos().add(alunoSalvo);
	        turma.setQtdAlunos(turma.getAlunos().size());
	        turmaRepo.save(turma);

	        // 7. Preparar resposta
	        Map<String, Object> response = new HashMap<>();
	        response.put("mensagem", "Aluno cadastrado e adicionado à turma com sucesso!");
	        response.put("aluno", usuarioMapper.toResponseDTO(alunoSalvo)); // Usar DTO, não Entity
	        response.put("turma", turma); // Considerar criar um TurmaDTO também

	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Erro ao cadastrar aluno na turma: " + e.getMessage());
	    }
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<?> criarAluno(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto) {
	    try {

	        if (userRep.existsByLogin(usuarioCreateDto.login())) {
	            return ResponseEntity.badRequest()
	                    .body("O email inserido já está em uso.");
	        }
	     
	        Usuario usuario = usuarioMapper.toEntityFromCreateDto(usuarioCreateDto);
	        
	        usuario.setPapel(PapelEnum.USER);
	        usuario.setCriadoEm(LocalDateTime.now());
	       
	        Usuario novoUser = userRep.save(usuario);
	        
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(usuarioMapper.toResponseDTO(novoUser));
	        
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Ocorreu um erro interno no sistema: " + e.getMessage());
	    }
	}
	
	@GetMapping("/")
    public ResponseEntity<?> buscarAlunos(){
        try {
            List<Usuario> usuarios = userRep.findByPapel(PapelEnum.USER);
            
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Nenhum usuário encontrado.");
            } 
            
            List<UsuarioResponseDto> usuarioDtos = usuarios.stream()
                    .map(usuarioMapper::toResponseDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(usuarioDtos);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro interno no servidor.");
        }
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarAlunoPorId(@PathVariable("id") Long id ){
		try {
			Optional<Usuario> usuario = userRep.findById(id);
			
			if(usuario.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Nenhum Aluno encontrado com o id: " + id);
			}
			
			 if (usuario.get().getPapel() != PapelEnum.USER) {
		            return ResponseEntity.status(HttpStatus.FORBIDDEN)
		                    .body("O usuário com o id " + id + " não tem o papel de ALUNO.");
		        }
			
			 return ResponseEntity.ok(usuarioMapper.toResponseDTO(usuario.get()));
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no sistema.");
		}
	}
	 
	 @PutMapping("/{id}")
	    public ResponseEntity<?> atualizarAluno(@PathVariable("id") Long id, @Valid @RequestBody UsuarioUpdateDto usuarioUpdateDto){
	        try {
	            
	        	Optional<Usuario> usuarioExistente = userRep.findById(id);
	            
	            if (usuarioExistente.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body("Usuário não encontrado");
	            }
	            
	            Usuario usuario = usuarioExistente.get();
	            
	            if (usuario.getPapel() != PapelEnum.USER) {
	                return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                        .body("O usuário com o id " + id + " não tem o papel de ALUNO.");
	            }
	            
	            // Verificar se o novo login já existe (se foi alterado)
	            if (!usuario.getLogin().equals(usuarioUpdateDto.login()) && 
	                userRep.existsByLogin(usuarioUpdateDto.login())) {
	                return ResponseEntity.badRequest()
	                        .body("O email inserido já está em uso.");
	            }
	            
	            // Atualizar campos usando o mapper
	            usuarioMapper.updateEntityFromUpdateDto(usuario, usuarioUpdateDto);
	            
	            Usuario usuarioAtualizado = userRep.save(usuario);
	            
	            return ResponseEntity.ok(usuarioMapper.toResponseDTO(usuarioAtualizado));
	            
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Ocorreu um erro interno no servidor: " + e.getMessage());
	        }
	    }
	 
	 @DeleteMapping("/{id}")
     public ResponseEntity<?> deletarAluno(@PathVariable("id") Long id){
         try {
             Optional<Usuario> verificaExiste = userRep.findById(id);
             
             if (verificaExiste.get().getPapel() != PapelEnum.USER) {
		            return ResponseEntity.status(HttpStatus.FORBIDDEN)
		                    .body("O usuário com o id " + id + " não tem o papel de ALUNO.");
		        }
             
             if(verificaExiste.isPresent()) {
                 userRep.deleteById(id);
                 
                 return ResponseEntity.ok("Usuário deletado com sucesso!");
             } else {
                 return ResponseEntity.status(HttpStatus.NO_CONTENT)
                         .body("Nenhum usuário foi encontrado.");
             }
             
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Ocorreu um erro interno no servidor.");
         }
     }

}
