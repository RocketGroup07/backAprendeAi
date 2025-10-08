package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.PostCreateDto;
import br.com.aprendeai.mappers.PostMapper;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.ArquivoService;

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
	
	@Autowired
    private ArquivoService arquivoService;
	
	@Autowired
	private PostMapper postMapper;
	
	@PostMapping("/criar/{usuarioId}/turma/{turmaId}")
	public ResponseEntity<?> criarPost(@PathVariable Long usuarioId,
	                      @PathVariable Long turmaId,
	                      @ModelAttribute PostCreateDto postRequest, 
	                      @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {
	    try {
	        Usuario autor = userRep.findById(usuarioId)
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
	        Turma turma = turmaRepo.findById(turmaId)
	                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

	        Arquivo arquivoSalvo = null;

	        if (arquivo != null && !arquivo.isEmpty()) {
	            arquivoSalvo = arquivoService.uploadArquivo(arquivo);
	            arquivoSalvo.setEnviadoPor(autor);
	        }

	        Post novoPost = postMapper.toEntityFromCreateDto(postRequest);
	        novoPost.setAutor(autor);
	        novoPost.setTurma(turma);
	        novoPost.setTitulo(postRequest.titulo()); 
	        novoPost.setConteudo(postRequest.conteudo());
	        novoPost.setPublico(postRequest.publico()); 
	        novoPost.setDataPostagem(LocalDateTime.now());

	        if (arquivoSalvo != null) {
	            novoPost.setPost(arquivoSalvo);
	        }
	        
	        System.out.println("DTO Recebido: " + postRequest);

	        Post postSalvo = postRepo.save(novoPost);
	        return ResponseEntity.ok(postSalvo);

	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage()); 
	    }
	}
	
	@GetMapping("/{postId}/{turmaId}")
	public ResponseEntity<?> listarPosts(@PathVariable Long postId, @PathVariable Long turmaId) {
		try {
			Optional<Post> post = postRepo.findByIdAndTurmaId(postId, turmaId);
			return ResponseEntity.ok(post);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Ocorreu um erro ao buscar o post com o id " + postId);
		}
	}


	@GetMapping("/turma/{turmaId}")
	public List<Post> listarPostsDaTurma(@PathVariable Long turmaId) {
	    return postRepo.findByTurmaIdAndPublicoTrue(turmaId);
	}
	
//	@GetMapping("/turma/{turmaId}/{postId}")
//	public List<Post> listarPostsDaTurma(@PathVariable Long turmaId, @PathVariable Long postId) {
//	    return postRepo.findByIdAndTurmaId(postId, turmaId);
//	}


	@GetMapping("/turma/{turmaId}/buscar")
	public List<Post> buscarNaTurmaPeloTitulo(@PathVariable Long turmaId,
	                                @RequestParam String titulo) {
	    return postRepo.findByTurmaIdAndTituloContainingIgnoreCaseAndPublicoTrueAndDataAgendadaBefore(
	            turmaId, titulo, LocalDateTime.now()
	    );
	}

	@DeleteMapping("/{turmaId}/{postid}")
	public ResponseEntity<?> deletarPost(@PathVariable Long turmaId, @PathVariable Long postId){
		Optional<Post> postExiste = postRepo.findByIdAndTurmaId(postId, turmaId);
		
		if(postExiste.isEmpty()) {
			return ResponseEntity.badRequest().body("Post não encontrado com o id " + postId);
		}
		
		Post postDelet = postExiste.get();
		
		postRepo.delete(postDelet);
		
		return ResponseEntity.ok(HttpStatus.OK);
		
	}
}
