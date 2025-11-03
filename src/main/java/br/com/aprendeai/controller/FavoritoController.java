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

import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.service.FavoritoService;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin
public class FavoritoController {

	private final FavoritoService favoritoService;

	public FavoritoController(FavoritoService favoritoService) {
		this.favoritoService = favoritoService;
	}

	@PostMapping("/adicionar/{postId}")
    public ResponseEntity<PostResponseDto> favoritar(@PathVariable Long postId) {
		return ResponseEntity.ok(favoritoService.favoritar(postId));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PostResponseDto>> listarFavoritos() {
        return ResponseEntity.ok(favoritoService.listarFavoritos());
    }
    
    @DeleteMapping("/remover/{favoritoId}")
    public ResponseEntity<?> removerFavorito(@PathVariable Long favoritoId) {
    	favoritoService.removerFavorito(favoritoId);
    	return ResponseEntity.noContent().build();
    }
}