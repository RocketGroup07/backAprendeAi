package br.com.aprendeai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public List<Usuario> findByPapel(PapelEnum Papel);
	
	public Usuario findByLogin(String login);

	boolean existsByLogin(String login);
	
	

}
