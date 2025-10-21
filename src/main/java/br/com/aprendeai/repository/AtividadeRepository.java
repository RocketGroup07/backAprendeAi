package br.com.aprendeai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aprendeai.model.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>{
	
	


}
