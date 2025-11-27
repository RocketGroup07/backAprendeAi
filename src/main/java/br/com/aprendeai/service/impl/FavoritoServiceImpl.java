package br.com.aprendeai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.FavoritosResponseDto;
import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.mappers.AtividadeMapper;
import br.com.aprendeai.mappers.PostMapper;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Favorito;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.AtividadeRepository;
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
	private final AtividadeMapper atividadeMapper;
	private final AtividadeRepository atividadeRepo;

	public FavoritoServiceImpl(AccessControlService accessControl, FavoritoRepository favoritoRepo,
			PostRepository postRepo, PostMapper postMapper, AtividadeMapper atividadeMapper,
			AtividadeRepository atividadeRepo) {
		this.accessControl = accessControl;
		this.favoritoRepo = favoritoRepo;
		this.postRepo = postRepo;
		this.postMapper = postMapper;
		this.atividadeMapper = atividadeMapper;
		this.atividadeRepo = atividadeRepo;
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

//	@Override
//	@Transactional
//	public void removerFavorito(Long favoritoId) {
//		favoritoRepo.findById(favoritoId)
//		.orElseThrow(() -> new RuntimeException("Favorito não encontrado"));
//
//		favoritoRepo.deleteById(favoritoId);
//	}
	
	@Transactional
	@Override
	public void removerFavoritoPost(Long postId) {
		Usuario usuario = accessControl.getUsuarioLogado();
		
		Favorito favorito = favoritoRepo.findByPostAndUsuarioId(postId, usuario.getId());
		favoritoRepo.delete(favorito);
	}
	
	@Transactional
	@Override
	public void removerFavoritoAtividade(Long atividadeId) {
		Usuario usuario = accessControl.getUsuarioLogado();
		
		Favorito favorito = favoritoRepo.findByAtividadeAndUsuarioId(atividadeId, usuario.getId());
		favoritoRepo.delete(favorito);
	}

	@Override
	@Transactional
	public AtividadeResponseDto favoritarAtv(Long atividadeId) {
		Usuario usuario = accessControl.getUsuarioLogado();
		
        Atividade atividade = atividadeRepo.findById(atividadeId).orElseThrow();

        boolean jaFavoritado = favoritoRepo.findByUsuario(usuario).stream()
                .anyMatch(f -> f.getPost().getId().equals(atividadeId));
        if (jaFavoritado) {
            return atividadeMapper.toResponseDTO(atividade);
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setAtividade(atividade);

        favoritoRepo.save(favorito);

        return atividadeMapper.toResponseDTO(atividade);
	}

	@Override
	@Transactional
	public List<AtividadeResponseDto> listarAtividadesFavs() {
		Usuario usuario = accessControl.getUsuarioLogado();
        List<Favorito> favoritos = favoritoRepo.findByUsuario(usuario);

        return favoritos.stream()
                .map(fav -> atividadeMapper.toResponseDTO(fav.getAtividade()))
                .toList();
	}
	
	@Override
	@Transactional
	public FavoritosResponseDto listarTodosFavoritos() {
	    Usuario usuario = accessControl.getUsuarioLogado();
	    List<Favorito> favoritos = favoritoRepo.findByUsuario(usuario);
	    
	    List<PostResponseDto> postsFav = favoritos.stream()
	            .map(Favorito::getPost)
	            .filter(java.util.Objects::nonNull) 
	            .map(postMapper::toResponseDto)
	            .toList();

	    List<AtividadeResponseDto> atividadesFav = favoritos.stream()
	            .map(Favorito::getAtividade)
	            .filter(java.util.Objects::nonNull) 
	            .map(atividadeMapper::toResponseDTO)
	            .toList();

	    return new FavoritosResponseDto(postsFav, atividadesFav);
	}

}
