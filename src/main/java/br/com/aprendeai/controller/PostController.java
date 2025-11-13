package br.com.aprendeai.controller;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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

import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.PostService;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
	
	private final PostService postService;
	
	private final ArquivoService arquivoService;
	
	public PostController(PostService postService, ArquivoService arquivoService) {
		this.postService = postService;
		this.arquivoService = arquivoService;
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
	
	@GetMapping("{turmaId}/{postId}/download/anexo")
    public ResponseEntity<Resource> baixarArquivoAnexo(@PathVariable Long turmaId, @PathVariable Long postId) throws IOException {
		Arquivo arquivo = postService.baixarAnexo(turmaId, postId);
		Resource arquivoAnexo = arquivoService.downloadArquivo(arquivo.getId());
		
		  Path caminhoArquivo = Paths.get(arquivo.getCaminhoArquivo());
		var contentType = Files.probeContentType(caminhoArquivo);
		
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
        		.body(arquivoAnexo);
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
