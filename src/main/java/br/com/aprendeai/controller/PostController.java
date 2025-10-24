package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.PostCreateDto;
import br.com.aprendeai.dtos.PostResponseDto;
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
	
	private final PostRepository postRepo;
	private final UsuarioRepository userRep;
	private final TurmaRepository turmaRepo;
    private final ArquivoService arquivoService;
	private final PostMapper postMapper;
	
	public PostController(PostRepository postRepo, UsuarioRepository userRep, TurmaRepository turmaRepo,
			ArquivoService arquivoService, PostMapper postMapper) {
		this.postRepo = postRepo;
		this.userRep = userRep;
		this.turmaRepo = turmaRepo;
		this.arquivoService = arquivoService;
		this.postMapper = postMapper;
	}

	@PostMapping("/criar/{usuarioId}/turma/{turmaId}")
	public ResponseEntity<?> criarPost(@PathVariable Long usuarioId,
	                      @PathVariable Long turmaId,
	                      @RequestPart(value = "post") PostCreateDto postRequest, 
	                      @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
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
	        
	        if(novoPost.getDataPostagem() == null) {
	        	novoPost.setDataPostagem(LocalDateTime.now());
	        }

	        if (arquivoSalvo != null) {
	            novoPost.setPost(arquivoSalvo);
	        }

	        Post postSalvo = postRepo.save(novoPost);
	        
	        PostResponseDto response = new PostResponseDto(
	                postSalvo.getId(),
	                turma.getId(),
	                postSalvo.getTitulo(),
	                postSalvo.getConteudo(),
	                autor.getNome(),
	                postSalvo.getDataPostagem()
	        );

	        return ResponseEntity.ok(response);

	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage()); 
	    }
	}
	
	@GetMapping("/{postId}/{turmaId}")
	public ResponseEntity<?> listarPostPorId(@PathVariable Long postId, @PathVariable Long turmaId) {
		try {
			Optional<Post> post = postRepo.findByIdAndTurmaId(postId, turmaId);
			if (post.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }

	        PostResponseDto response = postMapper.toResponseDto(post.get());
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Ocorreu um erro ao buscar o post com o id " + postId);
		}
	}


	@GetMapping("/turma/{turmaId}")
	public List<PostResponseDto> listarPostsDaTurma(@PathVariable Long turmaId) {
		List<Post> posts = postRepo.findByTurmaIdAndPublicoTrue(turmaId);
	    return posts.stream()
	                .map(postMapper::toResponseDto)
	                .toList();
	}
	
//	@GetMapping("/turma/{turmaId}/{postId}")
//	public List<Post> listarPostsDaTurma(@PathVariable Long turmaId, @PathVariable Long postId) {
//	    return postRepo.findByIdAndTurmaId(postId, turmaId);
//	}


	@GetMapping("/turma/{turmaId}/buscar")
	public List<PostResponseDto> buscarNaTurmaPeloTitulo(@PathVariable Long turmaId,
	                                @RequestParam String titulo) {
		List<Post> posts = postRepo.findByTurmaIdAndTituloContainingIgnoreCaseAndPublicoTrueAndDataAgendadaBefore(
	            turmaId, titulo, LocalDateTime.now()
	    );
	    return posts.stream()
	                .map(postMapper::toResponseDto)
	                .toList();
	}

	@DeleteMapping("/{turmaId}/{postId}")
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
