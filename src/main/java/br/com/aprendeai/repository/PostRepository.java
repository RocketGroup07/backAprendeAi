package br.com.aprendeai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	
	List<Post> findByTurmaIdAndPublicoTrueAndDataAgendadaBefore(Long turmaId, LocalDateTime agora);
	
	List<Post> findByTurmaIdAndTituloContainingIgnoreCaseAndPublicoTrueAndDataAgendadaBefore(Long turmaId, String titulo, LocalDateTime agora);

	List<Post> findByIdAndTurmaId(Long postId, Long turmaId);


}
