package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;

@RestController
@RequestMapping("/professores") // todas as requisicao de ADMIN
public class ProfessorController {

    @Autowired
    private UsuarioRepository userRep;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public ResponseEntity<?> criarProfessor(@RequestBody Usuario usuario) {
        try {
            if (usuario.getNome() == null || usuario.getNome().isEmpty()
                    || usuario.getLogin() == null || usuario.getLogin().isEmpty()
                    || usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                return ResponseEntity.badRequest().body("Preencha todos os campos.");
            }

            if (userRep.existsByLogin(usuario.getLogin())) {
                return ResponseEntity.badRequest().body("O email inserido já está em uso.");
            }
            
            var passwordHash = passwordEncoder.encode(usuario.getSenha());
            usuario.setPapel(PapelEnum.ADMIN); // Define como professor
            usuario.setCriadoEm(LocalDateTime.now());
            usuario.setSenha(passwordHash);
            Usuario novoProfessor = userRep.save(usuario);
            return ResponseEntity.ok(novoProfessor);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro interno no sistema.");
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> listarProfessores() {
        try {
            List<Usuario> professores = userRep.findAll()
                    .stream()
                    .filter(u -> u.getPapel() == PapelEnum.ADMIN)
                    .collect(Collectors.toList());

            if (professores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum professor encontrado.");
            }

            return ResponseEntity.ok(professores);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro interno no sistema.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProfessorPorId(@PathVariable("id") Long id) {
        try {
            Optional<Usuario> professor = userRep.findById(id);

            if (professor.isEmpty() || professor.get().getPapel() != PapelEnum.ADMIN) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Professor não encontrado com o id: " + id);
            }

            return ResponseEntity.ok(professor.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro interno no sistema.");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfessor(@PathVariable("id") Long id, @RequestBody Usuario usuarioAtualizado){
        try {
            
            Optional<Usuario> prof = userRep.findById(id);
            
            if (prof.get().getPapel() != PapelEnum.ADMIN) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Professor não encontrado com o id: " + id);
            }
            
            if (prof.isPresent()) {
                Usuario u = prof.get();
                
                if (!u.getLogin().equals(usuarioAtualizado.getLogin())) {
                    ResponseEntity<String> emailValidation = verificarEmail(usuarioAtualizado);
                    if (emailValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return emailValidation;
                    }
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
    public ResponseEntity<?> deletarProfessor(@PathVariable("id") Long id){
        try {
            Optional<Usuario> prof = userRep.findById(id);
            
            if (prof.get().getPapel() != PapelEnum.ADMIN) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Professor não encontrado com o id: " + id);
            }
            
            if(prof.isPresent()) {
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
