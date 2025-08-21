package br.com.aprendeai.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
	
	
	@Autowired
	private UsuarioRepository userRep;
	
	@PostMapping("/")
	public ResponseEntity<?> criarUsuario(Usuario usuario){
		try {
			
			if(usuario.getNome() == null || usuario.getNome().isEmpty() || usuario.getEmail() == null || usuario.getEmail().isEmpty()
					|| usuario.getSenha() == null || usuario.getSenha().isEmpty()){
				return ResponseEntity.badRequest().body("Preencha todos os campos.");
			}
			
			 ResponseEntity<String> emailValidation = verificarEmail(usuario);
	            if(emailValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
	                return emailValidation;
	         }
	            
	         Usuario novoUser = userRep.save(usuario);
	         
	         return ResponseEntity.ok(novoUser);
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no sistema.");
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<?> buscarUsuarioPorId(@PathVariable("id") Long id ){
		try {
			Optional<Usuario> usuario = userRep.findById(id);
			
			if(usuario.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Nenhum usuario encontrado com o id: " + id);
			}
			
			return ResponseEntity.ok(usuario.get());
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no sistema.");
		}
	}
	
	 private ResponseEntity<String> verificarEmail(Usuario usuario) {
         if (userRep.existsByEmail(usuario.getEmail())) {
             return ResponseEntity.badRequest()
                     .body("O email inserido já está em uso.");
         } else {
             return ResponseEntity.ok("Email validado!");
         }
     }
	

}
