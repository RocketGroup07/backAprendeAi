package br.com.aprendeai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.mappers.PostMapper;
import br.com.aprendeai.model.Favorito;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.FavoritoRepository;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.service.FavoritoService;
import jakarta.transaction.Transactional;

@Service
public class FavoritoServiceImpl implements FavoritoService{
	
	private final AccessControlService accessControl;
    private final FavoritoRepository favoritoRepo;
	private final PostRepository postRepo;
	private final PostMapper postMapper;

	public FavoritoServiceImpl(AccessControlService accessControl, FavoritoRepository favoritoRepo,
			PostRepository postRepo, PostMapper postMapper) {
		this.accessControl = accessControl;
		this.favoritoRepo = favoritoRepo;
		this.postRepo = postRepo;
		this.postMapper = postMapper;
	}

	@Override
	@Transactional
	public PostResponseDto favoritar(Long postId) {
		Usuario usuario = accessControl.getUsuarioLogado();
		
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

	@Override
	@Transactional
	public List<PostResponseDto> listarFavoritos() {
		Usuario usuario = accessControl.getUsuarioLogado();
        List<Favorito> favoritos = favoritoRepo.findByUsuario(usuario);

        return favoritos.stream()
                .map(fav -> postMapper.toResponseDto(fav.getPost()))
                .toList();
	}

	@Override
	@Transactional
	public void removerFavorito(Long favoritoId) {
		favoritoRepo.findById(favoritoId)
		.orElseThrow(() -> new RuntimeException("Favorito não encontrado"));

		favoritoRepo.deleteById(favoritoId);
	}

}
