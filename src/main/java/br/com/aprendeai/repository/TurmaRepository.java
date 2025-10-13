package br.com.aprendeai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long>{
	
	Optional<Turma> findByCodigo(String codigo);
	boolean existsByCodigo(String codigo);
	List<Turma> findByAlunos_Id(Long alunoId);
	List<Turma> findByProfessores_Id(Long id);

}