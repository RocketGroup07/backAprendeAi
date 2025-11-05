package br.com.aprendeai.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>{

	List<Atividade> findByTurma_Id(Long turmaId);
	
	@Query("SELECT a FROM Atividade a WHERE a.turma.id = :turmaId AND a.entregue = true")
	List<Atividade> findEntreguesByTurmaId(@Param("turmaId") Long turmaId);


}
