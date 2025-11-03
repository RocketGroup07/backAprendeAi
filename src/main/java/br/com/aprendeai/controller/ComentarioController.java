package br.com.aprendeai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.ComentarioCreateDto;
import br.com.aprendeai.dtos.ComentarioResponseDto;
import br.com.aprendeai.service.ComentarioService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin
public class ComentarioController {

	private final ComentarioService comentarioService;
	
	public ComentarioController(ComentarioService comentarioService) {
		this.comentarioService = comentarioService;
	}

	@PostMapping("/criar/{turmaId}/post/{postId}")
    public ResponseEntity<ComentarioResponseDto> criarComentario(@PathVariable Long turmaId,
                                                 @PathVariable Long postId,
                                                 @RequestBody ComentarioCreateDto comentarioDto) {
		
		return ResponseEntity.ok(comentarioService.criarComentario(turmaId, postId, comentarioDto));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ComentarioResponseDto>> listarComentarios(@PathVariable Long postId) {
        return ResponseEntity.ok(comentarioService.listarComentarios(postId));
    }

    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<?> deletarComentario(@PathVariable Long comentarioId) {
    	comentarioService.deletarComentario(comentarioId);
    	return ResponseEntity.noContent().build();
    }
	
}
