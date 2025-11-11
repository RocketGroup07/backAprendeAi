package br.com.aprendeai.controller;

import java.util.List;

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
import br.com.aprendeai.service.PostService;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/criar/{usuarioId}/turma/{turmaId}")
	public ResponseEntity<PostResponseDto> criarPost(@PathVariable Long usuarioId,
	                      @PathVariable Long turmaId,
	                      @RequestPart(value = "post") String post, 
	                      @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
	    	
    	return ResponseEntity.ok(postService.criarPost(usuarioId, turmaId, post, arquivo));
	}
	
	@GetMapping("/{postId}/{turmaId}")
	public ResponseEntity<PostResponseDto> listarPostPorId(@PathVariable Long postId, @PathVariable Long turmaId) {
		return ResponseEntity.ok(postService.buscarPostPorId(postId, turmaId));
	}

	@GetMapping("/turma/{turmaId}")
	public ResponseEntity<List<PostResponseDto>> listarPostsDaTurma(@PathVariable Long turmaId) {
		return ResponseEntity.ok(postService.listarPostsDaTurma(turmaId));
	}

	@GetMapping("/turma/{turmaId}/buscar")
	public ResponseEntity<List<PostResponseDto>> buscarNaTurmaPeloTitulo(@PathVariable Long turmaId,
	                                @RequestParam String titulo) {
		return ResponseEntity.ok(postService.buscarNaTurmaPeloTitulo(turmaId, titulo));
	}

	@DeleteMapping("/{turmaId}/{postId}")
	public ResponseEntity<?> deletarPost(@PathVariable Long turmaId, @PathVariable Long postId){
		postService.deletarPost(turmaId, postId);
		return ResponseEntity.noContent().build();
	}
}
