package br.com.aprendeai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>{
	
	public List<Atividade> findByTurmaId(Long turmaId);


}
