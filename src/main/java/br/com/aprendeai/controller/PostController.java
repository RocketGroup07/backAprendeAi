package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private UsuarioRepository userRep;
	
	@Autowired
	private TurmaRepository turmaRepo;
	
	@PostMapping("/criar/{usuarioId}/turma/{turmaId}")
	public Post criarPost(@PathVariable Long usuarioId,
	                      @PathVariable Long turmaId,
	                      @RequestBody Post post) {
	    Usuario autor = userRep.findById(usuarioId).orElseThrow();
	    Turma turma = turmaRepo.findById(turmaId).orElseThrow();

	    post.setAutor(autor);
	    post.setTurma(turma);

	    return postRepo.save(post);
	}


	@GetMapping("/turma/{turmaId}")
	public List<Post> listarPostsDaTurma(@PathVariable Long turmaId) {
	    return postRepo.findByTurmaIdAndPublicoTrueAndDataAgendadaBefore(turmaId, LocalDateTime.now());
	}
	
//	@GetMapping("/turma/{turmaId}/{postId}")
//	public List<Post> listarPostsDaTurma(@PathVariable Long turmaId, @PathVariable Long postId) {
//	    return postRepo.findByIdAndTurmaId(postId, turmaId);
//	}


	@GetMapping("/turma/{turmaId}/buscar")
	public List<Post> buscarNaTurma(@PathVariable Long turmaId,
	                                @RequestParam String titulo) {
	    return postRepo.findByTurmaIdAndTituloContainingIgnoreCaseAndPublicoTrueAndDataAgendadaBefore(
	            turmaId, titulo, LocalDateTime.now()
	    );
	}

}
