package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;

@RestController
@RequestMapping("/alunos") // todas as requisicoes de USER/ADMIN
@CrossOrigin
public class AlunoController {
	
	
	@Autowired
	private UsuarioRepository userRep;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/")
	public ResponseEntity<?> criarAluno(@RequestBody Usuario usuario){
		try {
			
			if(usuario.getNome() == null || usuario.getNome().isEmpty() || usuario.getLogin() == null || usuario.getLogin().isEmpty()
					|| usuario.getSenha() == null || usuario.getSenha().isEmpty()){
				return ResponseEntity.badRequest().body("Preencha todos os campos.");
			}
			
			 ResponseEntity<String> emailValidation = verificarEmail(usuario);
	            if(emailValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
	                return emailValidation;
	         }
	            
	         var passwordHash = passwordEncoder.encode(usuario.getSenha());
	            
	         usuario.setPapel(PapelEnum.USER);
	         
	         usuario.setCriadoEm(LocalDateTime.now());
	         
	         usuario.setSenha(passwordHash);
	            
	         Usuario novoUser = userRep.save(usuario);
	         
	         return ResponseEntity.ok(novoUser);
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no sistema.");
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
            
            return ResponseEntity.ok(usuarios);
            
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
			
			return ResponseEntity.ok(usuario.get());
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no sistema.");
		}
	}
	 
	 @PutMapping("/{id}")
	    public ResponseEntity<?> atualizarAluno(@PathVariable("id") Long id, @RequestBody Usuario usuarioAtualizado){
	        try {
	            
	            Optional<Usuario> verificaExiste = userRep.findById(id);
	            
	            if (verificaExiste.isPresent()) {
	                Usuario u = verificaExiste.get();
	                
	                if (!u.getLogin().equals(usuarioAtualizado.getLogin())) {
	                    ResponseEntity<String> emailValidation = verificarEmail(usuarioAtualizado);
	                    if (emailValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
	                        return emailValidation;
	                    }
	                }
	                
	                if (verificaExiste.get().getPapel() != PapelEnum.USER) {
			            return ResponseEntity.status(HttpStatus.FORBIDDEN)
			                    .body("O usuário com o id " + id + " não tem o papel de ALUNO.");
			        }
	                
	                u.setNome(usuarioAtualizado.getNome());
	                u.setLogin(usuarioAtualizado.getLogin());
	                
	                userRep.save(u);
	                
	                return ResponseEntity.ok(u);
	                
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body("Usuário não encontrado");
	            }
	            
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Ocorreu um erro interno no servidor.");
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
	 
	 private ResponseEntity<String> verificarEmail(Usuario usuario) {
         if (userRep.existsByLogin(usuario.getLogin())) {
             return ResponseEntity.badRequest()
                     .body("O email inserido já está em uso.");
         } else {
             return ResponseEntity.ok("Email validado!");
         }
     }

}
