package br.com.aprendeai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.aprendeai.model.Favorito;
import br.com.aprendeai.model.Usuario;

public interface FavoritoRepository extends JpaRepository<Favorito, Long>{
	
	List<Favorito> findByUsuario(Usuario usuario);

	Favorito findByPostId(Long postId);
	
	List<Favorito> findByUsuarioAndPostIsNotNull(Usuario usuario);

	Favorito findByPostIdAndUsuarioId(Long postId, Long usuarioId);

	Favorito findByAtividadeIdAndUsuarioId(Long atividadeId, Long usuarioId);

}
