package br.com.aprendeai.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.mappers.PostMapper;
import br.com.aprendeai.model.Favorito;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.FavoritoRepository;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.UsuarioRepository;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin
public class FavoritoController {

	
    private final FavoritoRepository favoritoRepo;
	private final UsuarioRepository usuarioRepo;
	private final PostRepository postRepo;
	private final PostMapper postMapper;

	public FavoritoController(FavoritoRepository favoritoRepo, UsuarioRepository usuarioRepo, PostRepository postRepo,
			PostMapper postMapper) {
		this.favoritoRepo = favoritoRepo;
		this.usuarioRepo = usuarioRepo;
		this.postRepo = postRepo;
		this.postMapper = postMapper;
	}

	@PostMapping("/adicionar/{usuarioId}/{postId}")
    public PostResponseDto favoritar(@PathVariable Long usuarioId, @PathVariable Long postId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        Post post = postRepo.findById(postId).orElseThrow();

        boolean jaFavoritado = favoritoRepo.findByUsuario(usuario).stream()
                .anyMatch(f -> f.getPost().getId().equals(postId));
        if (jaFavoritado) {
            return postMapper.toResponseDto(post);
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setPost(post);

        favoritoRepo.save(favorito);

        return postMapper.toResponseDto(post);
    }

    @GetMapping("/listar/{usuarioId}")
    public List<PostResponseDto> listarFavoritos(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        List<Favorito> favoritos = favoritoRepo.findByUsuario(usuario);

        return favoritos.stream()
                .map(fav -> postMapper.toResponseDto(fav.getPost()))
                .toList();
    }
}