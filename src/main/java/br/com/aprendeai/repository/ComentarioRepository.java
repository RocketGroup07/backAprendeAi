package br.com.aprendeai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
	List<Comentario> findByPostIdOrderByDataComentarioAsc(Long postId);
}
