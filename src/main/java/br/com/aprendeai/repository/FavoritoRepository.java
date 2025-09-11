package br.com.aprendeai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.aprendeai.model.Favorito;
import br.com.aprendeai.model.Usuario;

public interface FavoritoRepository extends JpaRepository<Favorito, Long>{
	
	List<Favorito> findByUsuario(Usuario usuario);

}
