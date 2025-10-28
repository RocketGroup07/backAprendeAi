package br.com.aprendeai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	
	@Query("SELECT p FROM Post p WHERE p.turma.id = :turmaId AND p.publico = true "
		     + "AND (p.dataPostagem IS NULL OR p.dataPostagem <= :dataAtual)")
	List<Post> findPublicadosByTurmaId(
       @Param("turmaId") Long turmaId, 
       @Param("dataAtual") LocalDateTime dataAtual 
   );

	@Query("SELECT p FROM Post p WHERE p.turma.id = :turmaId AND p.publico = true "
		     + "AND LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')) "
		     + "AND (p.dataPostagem IS NULL OR p.dataPostagem <= :dataAtual)")
	List<Post> buscarPublicadosByTitulo(
		    @Param("turmaId") Long turmaId,
		    @Param("titulo") String titulo,
		    @Param("dataAtual") LocalDateTime dataAtual);

	Optional<Post> findByIdAndTurmaId(Long postId, Long turmaId);



}
