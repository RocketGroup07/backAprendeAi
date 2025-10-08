package br.com.aprendeai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	
	List<Post> findByTurmaIdAndPublicoTrue(Long turmaId);
	
	List<Post> findByTurmaIdAndTituloContainingIgnoreCaseAndPublicoTrueAndDataAgendadaBefore(Long turmaId, String titulo, LocalDateTime agora);

	Optional<Post> findByIdAndTurmaId(Long postId, Long turmaId);


}
