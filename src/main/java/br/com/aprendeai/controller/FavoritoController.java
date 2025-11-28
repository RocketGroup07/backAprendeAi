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

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.FavoritosResponseDto;
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

	@PostMapping("/adicionar/post/{postId}")
    public ResponseEntity<PostResponseDto> favoritarPosts(@PathVariable Long postId) {
		return ResponseEntity.ok(favoritoService.favoritar(postId));
    }
	
	@PostMapping("/adicionar/atividade/{atividadeId}")
    public ResponseEntity<AtividadeResponseDto> favoritarAtv(@PathVariable Long atividadeId) {
		return ResponseEntity.ok(favoritoService.favoritarAtv(atividadeId));
    }

    @GetMapping("/listar/posts")
    public ResponseEntity<List<PostResponseDto>> listarPostsFavoritos() {
        return ResponseEntity.ok(favoritoService.listarFavoritos());
    }
    
    @GetMapping("/listar/")
    public ResponseEntity<FavoritosResponseDto> listarTodosFavoritos() {
        return ResponseEntity.ok(favoritoService.listarTodosFavoritos());
    }
    
    @GetMapping("/listar/atividades")
    public ResponseEntity<List<AtividadeResponseDto>> listarAtividadesFavoritos() {
        return ResponseEntity.ok(favoritoService.listarAtividadesFavs());
    }
    
    @DeleteMapping("/remover/posts/{postId}")
    public ResponseEntity<?> removerPostFavorito(@PathVariable Long postId) {
    	favoritoService.removerFavoritoPost(postId);
    	return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/remover/atividades/{atividadeId}")
    public ResponseEntity<?> removerAtividadeFavorita(@PathVariable Long atividadeId) {
    	favoritoService.removerFavoritoAtividade(atividadeId);
    	return ResponseEntity.noContent().build();
    }
    
    
}